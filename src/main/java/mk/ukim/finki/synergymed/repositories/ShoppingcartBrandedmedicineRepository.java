package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Shoppingcart;
import mk.ukim.finki.synergymed.models.ShoppingcartBrandedmedicine;
import mk.ukim.finki.synergymed.models.ShoppingcartBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingcartBrandedmedicineRepository extends JpaRepository<ShoppingcartBrandedmedicine, ShoppingcartBrandedmedicineId> {
    List<ShoppingcartBrandedmedicine> findAllByShoppingCart(Shoppingcart cart);
    void deleteAllByShoppingCart(Shoppingcart cart);
}