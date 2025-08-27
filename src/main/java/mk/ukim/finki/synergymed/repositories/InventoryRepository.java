package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}