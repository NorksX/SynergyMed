package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.UserRepository;
import mk.ukim.finki.synergymed.service.ClientOrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("orders")
public class ClientOrderController {

    private final ClientOrderService orderService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails ud) {
        return userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + ud.getUsername()));
    }

    @GetMapping
    public String myOrders(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getCurrentUser(ud);
        List<Clientorder> orders = orderService.findAllForClient(user.getId());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("{orderId}")
    public String myOrderDetail(@PathVariable Integer orderId,
                                @AuthenticationPrincipal UserDetails ud,
                                Model model) {
        User user = getCurrentUser(ud);
        Clientorder order = orderService.findByIdForClient(orderId, user.getId()).orElseThrow();
        model.addAttribute("order", order);
        model.addAttribute("payment", order.getPayment());
        model.addAttribute("deliveryCompany", order.getDeliveryCompany());
        return "order-detail";
    }
}
