package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicine;
import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyorderBrandedmedicineRepository extends JpaRepository<SupplyorderBrandedmedicine, SupplyorderBrandedmedicineId> {
}