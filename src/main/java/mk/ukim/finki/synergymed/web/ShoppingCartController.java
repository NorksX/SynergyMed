package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.repositories.ShoppingcartRepository;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import mk.ukim.finki.synergymed.service.ClubCardService; // NEW
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final BrandedMedicineService brandedmedicineService;
    private final ClientRepository clientRepository;
    private final ShoppingcartRepository shoppingcartRepository;
    private final ClubCardService clubCardService;

    @PostMapping("/add/{medicineId}")
    public String addToCart(@PathVariable Integer medicineId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.addMedicine(cart, medicine, quantity);
        return "redirect:/branded-medicines";
    }

    @PostMapping("/plus/{medicineId}")
    public String increaseQuantity(@PathVariable Integer medicineId, HttpSession session) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.addMedicine(cart, medicine, 1);
        return "redirect:/cart";
    }

    @PostMapping("/minus/{medicineId}")
    public String decreaseQuantity(@PathVariable Integer medicineId, HttpSession session) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.decreaseMedicine(cart, medicine);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{medicineId}")
    public String removeFromCart(@PathVariable Integer medicineId, HttpSession session) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.removeMedicine(cart, medicine);
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        Client client = getClientFromSession(session);
        Shoppingcart cart = getOrCreateCart(client);

        model.addAttribute("items", shoppingCartService.getMedicinesInCart(cart));
        model.addAttribute("total", shoppingCartService.getTotal(cart));
        model.addAttribute("username", session.getAttribute("username"));
        // Images not yet available as per original TODO
        model.addAttribute("firstImageById", null);

        clubCardService.getByClientId(client.getId())
                .ifPresent(card -> model.addAttribute("clubCard", card));

        return "cart";
    }

    private Client getClientFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        String username = (String) session.getAttribute("username");

        if (user == null || username == null) {
            throw new IllegalStateException("No user in session. Please login first.");
        }

        return clientRepository.findByUsers(user)
                .orElseThrow(() -> new IllegalStateException("Client not found for user " + username));
    }

    private Shoppingcart getOrCreateCart(Client client) {
        return shoppingcartRepository.findByClient(client)
                .orElseGet(() -> {
                    Shoppingcart cart = new Shoppingcart();
                    cart.setClient(client);
                    return shoppingcartRepository.save(cart);
                });
    }
}
