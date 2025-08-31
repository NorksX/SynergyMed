package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.service.ClientService;
import mk.ukim.finki.synergymed.service.DeliveryCompanyService;
import mk.ukim.finki.synergymed.service.PaymentMethodService;
import mk.ukim.finki.synergymed.service.PaymentService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
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

    @GetMapping
    public String getPaymentPage(Model model, HttpSession session) {

        model.addAttribute("methods", paymentMethodService.findAll());
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findAll());
        Client client = getClientFromSession(session);
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        model.addAttribute("total", shoppingCartService.getTotal(cart));

        return "payment";
    }



    @PostMapping
    public String processPayment(@RequestParam Integer paymentMethodId,
                                 @RequestParam Integer deliveryCompanyId,
                                 HttpSession session,
                                 Model model) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = shoppingCartService.getOrCreateCart(client);

        Clientorder order = paymentService.checkout(client, cart, paymentMethodId, deliveryCompanyId);

        model.addAttribute("order", order);
        model.addAttribute("payment", order.getPayment());
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