package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clubcard;
import mk.ukim.finki.synergymed.models.Contactinformation;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final HealthProfileService healthProfileService;
    private final ContactInformationService contactInformationService;
    private final SensitiveClientDataService sensitiveClientDataService;
    private final PrescriptionService prescriptionService;
    private final ClientService clientService;

    // NEW: Inject the ClubCardService
    private final ClubCardService clubCardService;

    @GetMapping
    public String getProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String username = (String) session.getAttribute("username");
        if (user == null || username == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("username", username);

        try {
            Optional<Healthprofile> healthProfile = healthProfileService.getByClientId(user.getId());
            model.addAttribute("healthProfile", healthProfile.orElse(null));
            model.addAttribute("hasHealthProfile", healthProfile.isPresent());
        } catch (Exception e) {
            model.addAttribute("hasHealthProfile", false);
        }

        // NEW: Load club card for the profile landing page
        try {
            Optional<Clubcard> clubcard = clubCardService.getByClientId(user.getId());
            model.addAttribute("clubcard", clubcard.orElse(null));
            model.addAttribute("hasClubcard", clubcard.isPresent());
        } catch (Exception e) {
            model.addAttribute("hasClubcard", false);
        }

        model.addAttribute("activeTab", "profile");
        return "profile";
    }

    // Contact Info tab
    @GetMapping("/contacts")
    public String profileContacts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Contactinformation> list = contactInformationService.listForUser(user.getId());
        Contactinformation contact = list.isEmpty() ? null : list.get(0);
        model.addAttribute("contact", contact);
        model.addAttribute("activeTab", "contacts");
        return "profile-contacts";
    }

    @GetMapping("/contacts/new")
    public String newProfileContact(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("context", "profile");
        model.addAttribute("postUrl", "/profile/contacts/save");
        model.addAttribute("backUrl", "/profile/contacts");
        return "contact-form";
    }

    @PostMapping("/contacts/save")
    public String saveProfileContact(@RequestParam(required = false) Integer id,
                                     @RequestParam(required = false) String phone,
                                     @RequestParam(required = false) String address,
                                     HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (id == null) {
            contactInformationService.createForUser(user.getId(), phone, address);
        } else {
            contactInformationService.updateForUser(id, user.getId(), phone, address);
        }
        return "redirect:/profile/contacts";
    }

    @PostMapping("/contacts/delete")
    public String deleteProfileContact(@RequestParam Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        contactInformationService.deleteForUser(id, user.getId());
        return "redirect:/profile/contacts";
    }

    private boolean isVerified(Integer userId) {
        return clientService.isVerified(userId);
    }

    @GetMapping("/prescriptions")
    public String prescriptions(jakarta.servlet.http.HttpSession session,
                                org.springframework.ui.Model model) {
        var user = (mk.ukim.finki.synergymed.models.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Integer clientId = user.getId();

        boolean verified = isVerified(clientId);
        boolean pending = sensitiveClientDataService.latestForClient(clientId)
                .map(s -> "во тек".equalsIgnoreCase(s.getVerificationStatus()))
                .orElse(false);

        var rx = verified ? prescriptionService.listForClient(clientId) : java.util.List.of();

        model.addAttribute("activeTab", "prescriptions");
        model.addAttribute("verified", verified);
        model.addAttribute("pending", pending);
        model.addAttribute("prescriptions", rx);
        return "profile-prescriptions";
    }

    @GetMapping("/clubcard")
    public String profileClubcard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Optional<Clubcard> clubcard = clubCardService.getByClientId(user.getId());
        model.addAttribute("clubcard", clubcard.orElse(null));
        model.addAttribute("hasClubcard", clubcard.isPresent());
        model.addAttribute("activeTab", "clubcard");
        return "profile-clubcard";
    }

    @PostMapping("/clubcard/create")
    public String createClubcard(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (clubCardService.getByClientId(user.getId()).isEmpty()) {
            Client client = clientService.findClientById(user.getId());
            clubCardService.createForClient(client);
        }
        return "redirect:/profile/clubcard";
    }
}
