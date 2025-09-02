package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Shoppingcart;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface ShoppingCartService {
    void addMedicine(Shoppingcart cart, Brandedmedicine medicine, int quantity);
    void removeMedicine(Shoppingcart cart, Brandedmedicine medicine);
    Map<Brandedmedicine, Integer> getMedicinesInCart(Shoppingcart cart);
    BigDecimal getTotal(Shoppingcart cart);
    void clearCart(Shoppingcart cart);
    public void decreaseMedicine(Shoppingcart cart, Brandedmedicine medicine);
    Shoppingcart getOrCreateCart(Client client);
    Map<Integer, Integer> getMaxAvailableFor(Collection<Integer> brandedMedicineIds);

}
