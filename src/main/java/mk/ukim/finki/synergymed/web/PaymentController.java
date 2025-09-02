package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.ClubCardService;
import mk.ukim.finki.synergymed.service.DeliveryCompanyService;
import mk.ukim.finki.synergymed.service.PaymentMethodService;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ShoppingCartService shoppingCartService;
    private final PaymentMethodService paymentMethodService;
    private final DeliveryCompanyService deliveryCompanyService;
    private final ClientService clientService;
    private final UserRepository userRepository;          // from repo auth
    private final ClubCardService clubCardService;        // from local logic

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping
    public String getPaymentPage(@RequestParam(name = "useCard", defaultValue = "false") boolean useCard,
                                 @AuthenticationPrincipal UserDetails ud,
                                 Model model) {
        // Resolve current user/client using repo auth strategy
        User user = getCurrentUser(ud);
        Client client = clientService.findClientById(user.getId());
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        // Local discount logic with club card
        int base = shoppingCartService.getTotal(cart).intValue();
        int discount = 0;
        if (useCard) {
            var cardOpt = clubCardService.getByClientId(client.getId());
            if (cardOpt.isPresent()) {
                Integer pts = cardOpt.get().getPoints();
                int points = pts == null ? 0 : pts;
                discount = Math.min(points / 2, base);
            }
        }
        int shown = Math.max(0, base - discount);

        model.addAttribute("methods", paymentMethodService.findAll());
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findAll());
        model.addAttribute("total", BigDecimal.valueOf(shown));
        model.addAttribute("discount", discount);
        model.addAttribute("useCard", useCard);
        model.addAttribute("username", ud.getUsername());
        return "payment";
    }

    @PostMapping
    public String processPayment(@AuthenticationPrincipal UserDetails ud,
                                 @RequestParam Integer paymentMethodId,
                                 @RequestParam Integer deliveryCompanyId,
                                 @RequestParam(name = "useCard", defaultValue = "false") boolean useCard,
                                 Model model) {
        // Resolve current user/client using repo auth strategy
        User user = getCurrentUser(ud);
        Client client = clientService.findClientById(user.getId());
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        // Pass useCard through to service as in local logic
        Clientorder order = paymentService.checkout(client, cart, paymentMethodId, deliveryCompanyId, useCard);

        model.addAttribute("order", order);
        model.addAttribute("payment", order.getPayment());
        model.addAttribute("username", ud.getUsername());
        return "payment-success";
    }
}
