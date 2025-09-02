package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.repositories.ShoppingcartRepository;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private Client getClient(UserDetails ud) {
        User user = clientRepository.findByUsersUsername(ud.getUsername())
                .map(Client::getUsers)
                .orElseThrow(() -> new IllegalStateException("Client not found for user " + ud.getUsername()));
        return clientRepository.findByUsers(user)
                .orElseThrow(() -> new IllegalStateException("Client not found for user " + ud.getUsername()));
    }

    private Shoppingcart getOrCreateCart(Client client) {
        return shoppingcartRepository.findByClient(client)
                .orElseGet(() -> {
                    Shoppingcart cart = new Shoppingcart();
                    cart.setClient(client);
                    return shoppingcartRepository.save(cart);
                });
    }

    @PostMapping("/add/{medicineId}")
    public String addToCart(@PathVariable Integer medicineId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @AuthenticationPrincipal UserDetails ud) {
        Client client = getClient(ud);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.addMedicine(cart, medicine, quantity);
        return "redirect:/branded-medicines";
    }

    @PostMapping("/plus/{medicineId}")
    public String increaseQuantity(@PathVariable Integer medicineId,
                                   @AuthenticationPrincipal UserDetails ud) {
        Client client = getClient(ud);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.addMedicine(cart, medicine, 1);
        return "redirect:/cart";
    }

    @PostMapping("/minus/{medicineId}")
    public String decreaseQuantity(@PathVariable Integer medicineId,
                                   @AuthenticationPrincipal UserDetails ud) {
        Client client = getClient(ud);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.decreaseMedicine(cart, medicine);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{medicineId}")
    public String removeFromCart(@PathVariable Integer medicineId,
                                 @AuthenticationPrincipal UserDetails ud) {
        Client client = getClient(ud);
        Shoppingcart cart = getOrCreateCart(client);

        Brandedmedicine medicine = brandedmedicineService.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id " + medicineId));

        shoppingCartService.removeMedicine(cart, medicine);
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model,
                           @AuthenticationPrincipal UserDetails ud) {
        Client client = getClient(ud);
        Shoppingcart cart = getOrCreateCart(client);

        model.addAttribute("items", shoppingCartService.getMedicinesInCart(cart));
        model.addAttribute("total", shoppingCartService.getTotal(cart));
        model.addAttribute("username", ud.getUsername());
        model.addAttribute("firstImageById", null);

        return "cart";
    }
}