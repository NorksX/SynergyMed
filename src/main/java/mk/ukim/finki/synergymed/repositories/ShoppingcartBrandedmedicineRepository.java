package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.ShoppingcartBrandedmedicine;
import mk.ukim.finki.synergymed.models.ShoppingcartBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingcartBrandedmedicineRepository extends JpaRepository<ShoppingcartBrandedmedicine, ShoppingcartBrandedmedicineId> {
}