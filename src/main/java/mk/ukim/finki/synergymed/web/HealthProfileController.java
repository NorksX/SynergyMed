package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/health-profile")
@RequiredArgsConstructor
public class HealthProfileController {

    private final HealthProfileService healthProfileService;
    private final ClientService clientService;

    // TODO: 28.8.2025 Only admins can access this
    @GetMapping("/create")
    public String getCreateHealthProfilePage(
            @RequestParam(required = false) String searchTerm,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // Get clients without health profiles
        List<Client> clientsWithoutHealthProfile;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            clientsWithoutHealthProfile = clientService.findClientsWithoutHealthProfileByName(searchTerm);
            model.addAttribute("searchTerm", searchTerm);
            model.addAttribute("searched", true);
        } else {
            clientsWithoutHealthProfile = clientService.findAllClientsWithoutHealthProfile();
            model.addAttribute("searched", false);
        }

        model.addAttribute("clients", clientsWithoutHealthProfile);

        return "create-health-profile";
    }

    @PostMapping("/create")
    public String createHealthProfile(
            @RequestParam Integer clientId,
            @RequestParam String bloodType,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        // TODO: Add admin role check here

        try {
            Client client = clientService.findClientById(clientId);
            healthProfileService.createForClient(client, bloodType);

            redirectAttributes.addFlashAttribute("success",
                    "Health profile created successfully.");

            return "redirect:/admin/health-profile/create";

        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create health profile: " + e.getMessage());
            return "redirect:/admin/health-profile/create";
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Client> searchClients(@RequestParam String term) {
        if (term == null || term.trim().isEmpty()) {
            return clientService.findAllClientsWithoutHealthProfile();
        }
        return clientService.findClientsWithoutHealthProfileByName(term);
    }
}