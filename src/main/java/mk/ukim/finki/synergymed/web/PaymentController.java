package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.ClubCardService;
import mk.ukim.finki.synergymed.service.DeliveryCompanyService;
import mk.ukim.finki.synergymed.service.PaymentMethodService;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
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
    private final ClubCardService clubCardService; // NEW

    @GetMapping
    public String getPaymentPage(@RequestParam(name="useCard", defaultValue="false") boolean useCard,
                                 Model model, HttpSession session) {
        model.addAttribute("methods", paymentMethodService.findAll());
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findAll());

        Client client = getClientFromSession(session);
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        int base = shoppingCartService.getTotal(cart).intValue();
        int discount = 0;
        if (useCard) {
            var card = clubCardService.getByClientId(client.getId());
            if (card.isPresent()) {
                Integer pts = card.get().getPoints();
                int points = pts == null ? 0 : pts;
                discount = Math.min(points / 2, base);
            }
        }
        int shown = Math.max(0, base - discount);

        model.addAttribute("total", BigDecimal.valueOf(shown));
        model.addAttribute("discount", discount);
        model.addAttribute("useCard", useCard);
        model.addAttribute("username", session.getAttribute("username"));
        return "payment";
    }

    @PostMapping
    public String processPayment(@RequestParam Integer paymentMethodId,
                                 @RequestParam Integer deliveryCompanyId,
                                 @RequestParam(name="useCard", defaultValue="false") boolean useCard,
                                 HttpSession session, Model model) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);
        Clientorder order = paymentService.checkout(client, cart, paymentMethodId, deliveryCompanyId, useCard);
        model.addAttribute("order", order);
        model.addAttribute("payment", order.getPayment());
        model.addAttribute("username", session.getAttribute("username"));
        return "payment-success";
    }

    private Client getClientFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        String username = (String) session.getAttribute("username");
        if (user == null || username == null) {
            throw new IllegalStateException("No user in session. Please login first.");
        }
        return clientService.findClientById(user.getId());
    }
}
