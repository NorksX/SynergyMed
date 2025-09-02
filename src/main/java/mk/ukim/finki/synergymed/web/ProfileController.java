package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Contactinformation;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final HealthProfileService healthProfileService;
    private final ContactInformationService contactInformationService;
    private final SensitiveClientDataService sensitiveClientDataService;
    private final PrescriptionService prescriptionService;
    private final ClientService clientService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String username;

        if (principal instanceof UserDetails ud) {
            username = ud.getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @GetMapping
    public String getProfilePage(Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());

        try {
            Optional<Healthprofile> healthProfile = healthProfileService.getByClientId(user.getId());
            model.addAttribute("healthProfile", healthProfile.orElse(null));
            model.addAttribute("hasHealthProfile", healthProfile.isPresent());
        } catch (Exception e) {
            model.addAttribute("hasHealthProfile", false);
        }
        model.addAttribute("activeTab", "profile");
        return "profile";
    }

    @GetMapping("/contacts")
    public String profileContacts(Model model) {
        User user = getCurrentUser();
        List<Contactinformation> list = contactInformationService.listForUser(user.getId());
        Contactinformation contact = list.isEmpty() ? null : list.get(0);
        model.addAttribute("contact", contact);
        model.addAttribute("activeTab", "contacts");
        return "profile-contacts";
    }

    @GetMapping("/contacts/new")
    public String newProfileContact(Model model) {
        getCurrentUser(); // just to ensure authenticated
        model.addAttribute("context", "profile");
        model.addAttribute("postUrl", "/profile/contacts/save");
        model.addAttribute("backUrl", "/profile/contacts");
        return "contact-form";
    }

    @PostMapping("/contacts/save")
    public String saveProfileContact(@RequestParam(required = false) Integer id,
                                     @RequestParam(required = false) String phone,
                                     @RequestParam(required = false) String address) {
        User user = getCurrentUser();
        if (id == null) {
            contactInformationService.createForUser(user.getId(), phone, address);
        } else {
            contactInformationService.updateForUser(id, user.getId(), phone, address);
        }
        return "redirect:/profile/contacts";
    }

    @PostMapping("/contacts/delete")
    public String deleteProfileContact(@RequestParam Integer id) {
        User user = getCurrentUser();
        contactInformationService.deleteForUser(id, user.getId());
        return "redirect:/profile/contacts";
    }

    private boolean isVerified(Integer userId) {
        return clientService.isVerified(userId);
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {
        User user = getCurrentUser();
        Integer clientId = user.getId();

        boolean verified = isVerified(clientId);
        boolean pending = sensitiveClientDataService.latestForClient(clientId)
                .map(s -> "во тек".equalsIgnoreCase(s.getVerificationStatus()))
                .orElse(false);

        var rx = verified ? prescriptionService.listForClient(clientId) : List.of();

        model.addAttribute("activeTab", "prescriptions");
        model.addAttribute("verified", verified);
        model.addAttribute("pending", pending);
        model.addAttribute("prescriptions", rx);
        return "profile-prescriptions";
    }
}