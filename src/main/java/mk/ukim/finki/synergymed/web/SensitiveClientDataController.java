package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.SensitiveClientDataService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile/verification")
@RequiredArgsConstructor
public class SensitiveClientDataController {

    private final SensitiveClientDataService sensitiveService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping("/apply")
    public String applyForm(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("activeTab", "prescriptions");
        return "verification-apply";
    }

    @PostMapping("/apply")
    public String submitApplication(@RequestParam String embg,
                                    @RequestParam("portrait") MultipartFile portrait,
                                    @AuthenticationPrincipal UserDetails ud,
                                    RedirectAttributes ra) {
        User user = getCurrentUser(ud);
        try {
            sensitiveService.applyOrUpdate(user.getId(), embg, portrait);
            ra.addFlashAttribute("message", "Application submitted. Verification is now pending.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/verification/apply";
        }
        return "redirect:/profile/prescriptions";
    }
}