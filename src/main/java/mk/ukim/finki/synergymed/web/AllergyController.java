package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.MedicineRepository;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/allergies")
@RequiredArgsConstructor
public class AllergyController {

    private final HealthProfileService healthProfileService;
    private final MedicineRepository medicineRepository;

    @GetMapping("/manage")
    public String manageAllergies(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String username = (String) session.getAttribute("username");

        if (user == null || username == null) {
            System.out.println("NO USER IN SESSION");
            return "redirect:/login";
        }

        // Check if user has a health profile
        Optional<Healthprofile> healthProfile = healthProfileService.getByClientId(user.getId());
        if (healthProfile.isEmpty()) {
            model.addAttribute("error", "No health profile found. Please contact your healthcare provider.");
            return "redirect:/profile";
        }

        // Get all available medicines for the dropdown
        List<Medicine> medicines = medicineRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("username", username);
        model.addAttribute("healthProfile", healthProfile.get());
        model.addAttribute("medicines", medicines);

        return "manage-allergies";
    }

    @PostMapping("/add")
    public String addAllergy(@RequestParam Integer medicineId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDiagnosed,
                             @RequestParam String description,
                             @RequestParam String severity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

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