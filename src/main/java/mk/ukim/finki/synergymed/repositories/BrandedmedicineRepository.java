package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Brandedmedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandedmedicineRepository extends JpaRepository<Brandedmedicine, Integer> {
}