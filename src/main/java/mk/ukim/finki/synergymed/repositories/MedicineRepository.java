package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    Optional<Medicine> getMedicineById(Integer id);
    Optional<Medicine> getMedicineByMedicineName(String name);
    boolean existsByMedicineName(String name);
}