package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Shoppingcart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingcartRepository extends JpaRepository<Shoppingcart, Integer> {
}