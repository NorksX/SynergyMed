package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.InventoryBrandedmedicine;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryBrandedmedicineRepository extends JpaRepository<InventoryBrandedmedicine, InventoryBrandedmedicineId> {
}