package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pharmacist/health-profile")
@RequiredArgsConstructor
public class HealthProfileController {

    private final HealthProfileService healthProfileService;
    private final ClientService clientService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping("/create")
    public String getCreateHealthProfilePage(
            @RequestParam(required = false) String searchTerm,
            @AuthenticationPrincipal UserDetails ud,
            Model model) {

        User user = getCurrentUser(ud);
        model.addAttribute("user", user);

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
            @AuthenticationPrincipal UserDetails ud,
            RedirectAttributes redirectAttributes) {

        User user = getCurrentUser(ud);

        try {
            Client client = clientService.findClientById(clientId);
            healthProfileService.createForClient(client, bloodType);

            redirectAttributes.addFlashAttribute("success",
                    "Health profile created successfully.");

            return "redirect:/pharmacist/health-profile/create";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create health profile: " + e.getMessage());
            return "redirect:/pharmacist/health-profile/create";
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