package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.DeliveryCompanyService;
import mk.ukim.finki.synergymed.service.PaymentMethodService;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ShoppingCartService shoppingCartService;
    private final PaymentMethodService paymentMethodService;
    private final DeliveryCompanyService deliveryCompanyService;
    private final ClientService clientService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping
    public String getPaymentPage(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        Client client = clientService.findClientById(user.getId());
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        model.addAttribute("methods", paymentMethodService.findAll());
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findAll());
        model.addAttribute("total", shoppingCartService.getTotal(cart));

        return "payment";
    }

    @PostMapping
    public String processPayment(@AuthenticationPrincipal UserDetails ud,
                                 @RequestParam Integer paymentMethodId,
                                 @RequestParam Integer deliveryCompanyId,
                                 Model model) {
        User user = getCurrentUser(ud);
        Client client = clientService.findClientById(user.getId());
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        Clientorder order = paymentService.checkout(client, cart, paymentMethodId, deliveryCompanyId);

        model.addAttribute("order", order);
        model.addAttribute("payment", order.getPayment());
        return "payment-success";
    }
}