package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
}