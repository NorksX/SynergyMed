package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Shoppingcart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingcartRepository extends JpaRepository<Shoppingcart, Integer> {
    Optional<Shoppingcart> findByClient(Client client);
}