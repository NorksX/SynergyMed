package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.DistributorBrandedmedicine;
import mk.ukim.finki.synergymed.models.DistributorBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorBrandedmedicineRepository extends JpaRepository<DistributorBrandedmedicine, DistributorBrandedmedicineId> {
}