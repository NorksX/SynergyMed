package mk.ukim.finki.synergymed.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.*;
import mk.ukim.finki.synergymed.repositories.ShoppingcartBrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.ShoppingcartRepository;
import mk.ukim.finki.synergymed.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingcartBrandedmedicineRepository cartMedicineRepo;
    private final ShoppingcartRepository shoppingcartRepo;

    @Override
    public void addMedicine(Shoppingcart cart, Brandedmedicine medicine, int quantity) {
        ShoppingcartBrandedmedicineId id =
                new ShoppingcartBrandedmedicineId(cart.getId(), medicine.getId());
        ShoppingcartBrandedmedicine entry =
                cartMedicineRepo.findById(id).orElse(new ShoppingcartBrandedmedicine());

        if (entry.getId() == null) {
            entry.setId(id);
            entry.setShoppingCart(cart);
            entry.setBrandedMedicine(medicine);
            entry.setQuantity(quantity);
        } else {
            entry.setQuantity(entry.getQuantity() + quantity);
        }

        // TODO: 30.8.2025 stock check with trigger

        cartMedicineRepo.save(entry);
    }

    @Override
    public void decreaseMedicine(Shoppingcart cart, Brandedmedicine medicine) {
        ShoppingcartBrandedmedicineId id =
                new ShoppingcartBrandedmedicineId(cart.getId(), medicine.getId());
        cartMedicineRepo.findById(id).ifPresent(entry -> {
            if (entry.getQuantity() > 1) {
                entry.setQuantity(entry.getQuantity() - 1);
                cartMedicineRepo.save(entry);
            } else {
                cartMedicineRepo.delete(entry);
            }
        });
    }

    @Override
    public void removeMedicine(Shoppingcart cart, Brandedmedicine medicine) {
        ShoppingcartBrandedmedicineId id =
                new ShoppingcartBrandedmedicineId(cart.getId(), medicine.getId());
        cartMedicineRepo.deleteById(id);
    }

    @Override
    public Map<Brandedmedicine, Integer> getMedicinesInCart(Shoppingcart cart) {
        List<ShoppingcartBrandedmedicine> entries =
                cartMedicineRepo.findAllByShoppingCart(cart);
        Map<Brandedmedicine, Integer> result = new HashMap<>();
        for (ShoppingcartBrandedmedicine e : entries) {
            result.put(e.getBrandedMedicine(), e.getQuantity());
        }
        return result;
    }

    @Override
    public BigDecimal getTotal(Shoppingcart cart) {
        return getMedicinesInCart(cart).entrySet().stream()
                .map(entry -> entry.getKey().getPrice()
                        .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Shoppingcart getOrCreateCart(Client client) {
        return shoppingcartRepo.findByClient(client)
                .orElseGet(() -> {
                    Shoppingcart cart = new Shoppingcart();
                    cart.setClient(client);
                    return shoppingcartRepo.save(cart);
                });
    }

    @Override
    public void clearCart(Shoppingcart cart) {
        cartMedicineRepo.deleteAllByShoppingCart(cart);
    }
}