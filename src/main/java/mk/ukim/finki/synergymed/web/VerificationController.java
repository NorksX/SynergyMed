package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import mk.ukim.finki.synergymed.service.VerificationReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationReviewService reviewService;
    @GetMapping
    public String list(Model model) {
        List<Sensitiveclientdata> pending = reviewService.listPending();
        model.addAttribute("pending", pending);
        return "verification-list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Sensitiveclientdata row = reviewService.get(id).orElseThrow();
        model.addAttribute("item", row);
        return "verification-approval";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Integer id) {
        reviewService.approve(id);
        return "redirect:/admin/verification";
    }

    @PostMapping("/{id}/deny")
    public String deny(@PathVariable Integer id) {
        reviewService.deny(id);
        return "redirect:/admin/verification";
    }
}
