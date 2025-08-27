package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.BrandedMedicineInstanceofMedicine;
import mk.ukim.finki.synergymed.models.BrandedMedicineInstanceofMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandedMedicineInstanceofMedicineRepository extends JpaRepository<BrandedMedicineInstanceofMedicine, BrandedMedicineInstanceofMedicineId> {
}