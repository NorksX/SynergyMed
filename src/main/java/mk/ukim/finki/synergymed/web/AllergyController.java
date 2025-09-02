package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.MedicineRepository;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/allergies")
@RequiredArgsConstructor
public class AllergyController {

    private final HealthProfileService healthProfileService;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping("/manage")
    public String manageAllergies(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);

        Optional<Healthprofile> healthProfile = healthProfileService.getByClientId(user.getId());
        if (healthProfile.isEmpty()) {
            model.addAttribute("error", "No health profile found. Please contact your healthcare provider.");
            return "redirect:/profile";
        }

        List<Medicine> medicines = medicineRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("healthProfile", healthProfile.get());
        model.addAttribute("medicines", medicines);

        return "manage-allergies";
    }

    @PostMapping("/add")
    public String addAllergy(@AuthenticationPrincipal UserDetails ud,
                             @RequestParam Integer medicineId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDiagnosed,
                             @RequestParam String description,
                             @RequestParam String severity,
                             RedirectAttributes redirectAttributes) {

        User user = getCurrentUser(ud);

        try {
            healthProfileService.addAllergy(
                    user.getId(),
                    medicineId,
                    dateDiagnosed,
                    description,
                    severity
            );

            redirectAttributes.addFlashAttribute("success", "Allergy added successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add allergy: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}