package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Clubcard;
import mk.ukim.finki.synergymed.models.Contactinformation;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ClubCardService clubcardService;


    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
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
    public String profileContacts(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        List<Contactinformation> list = contactInformationService.listForUser(user.getId());
        Contactinformation contact = list.isEmpty() ? null : list.get(0);
        model.addAttribute("contact", contact);
        model.addAttribute("activeTab", "contacts");
        return "profile-contacts";
    }

    @GetMapping("/contacts/new")
    public String newProfileContact() {
        return "contact-form";
    }

    @PostMapping("/contacts/save")
    public String saveProfileContact(@AuthenticationPrincipal UserDetails ud,
                                     @RequestParam(required = false) Integer id,
                                     @RequestParam(required = false) String phone,
                                     @RequestParam(required = false) String address) {
        User user = getCurrentUser(ud);
        if (id == null) {
            contactInformationService.createForUser(user.getId(), phone, address);
        } else {
            contactInformationService.updateForUser(id, user.getId(), phone, address);
        }
        return "redirect:/profile/contacts";
    }

    @PostMapping("/contacts/delete")
    public String deleteProfileContact(@AuthenticationPrincipal UserDetails ud,
                                       @RequestParam Integer id) {
        User user = getCurrentUser(ud);
        contactInformationService.deleteForUser(id, user.getId());
        return "redirect:/profile/contacts";
    }

    private boolean isVerified(Integer userId) {
        return clientService.isVerified(userId);
    }

    @GetMapping("/prescriptions")
    public String prescriptions(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
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
    @GetMapping("/clubcard")
    public String clubcard(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());

        Optional<Clubcard> opt = clubcardService.getByClientId(user.getId());
        model.addAttribute("hasClubcard", opt.isPresent());
        model.addAttribute("clubcard", opt.orElse(null));

        model.addAttribute("activeTab", "clubcard");
        return "profile-clubcard";

    }
    @PostMapping("/clubcard/create")
    public String createClubcard(@AuthenticationPrincipal UserDetails ud,
                                 @RequestParam("program") String program) {
        User user = getCurrentUser(ud);

        Optional<Clubcard> existing = clubcardService.getByClientId(user.getId());
        if (existing.isEmpty()) {
            clubcardService.createForClient(user.getId(), program);
        }

        return "redirect:/profile/clubcard";
    }

}