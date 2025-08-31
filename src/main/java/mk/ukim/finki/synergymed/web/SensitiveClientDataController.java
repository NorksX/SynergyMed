package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.SensitiveClientDataService;
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

    @GetMapping("/apply")
    public String applyForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // require login [18].
        model.addAttribute("activeTab", "prescriptions");
        return "verification-apply";
    }

    @PostMapping("/apply")
    public String submitApplication(@RequestParam String embg,
                                    @RequestParam("portrait") MultipartFile portrait,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        try {
            sensitiveService.applyOrUpdate(user.getId(), embg, portrait); // single-row upsert [13].
            ra.addFlashAttribute("message", "Application submitted. Verification is now pending.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/verification/apply";
        }
        return "redirect:/profile/prescriptions";
    }
}
