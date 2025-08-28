package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final HealthProfileService healthProfileService;

    @GetMapping
    public String getProfilePage(HttpSession session, Model model) {
        // Get your custom User entity directly from session
        User user = (User) session.getAttribute("user");
        String username = (String) session.getAttribute("username");

        if (user == null || username == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("username", username);

        try {
            Optional<Healthprofile> healthProfile = healthProfileService.getByClientId(user.getId());

            if (healthProfile.isPresent()) {
                model.addAttribute("healthProfile", healthProfile.get());
                model.addAttribute("hasHealthProfile", true);
            } else {
                model.addAttribute("hasHealthProfile", false);
            }
        } catch (Exception e) {
            model.addAttribute("hasHealthProfile", false);
        }

        return "profile";
    }
}