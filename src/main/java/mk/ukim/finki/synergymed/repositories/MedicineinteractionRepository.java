package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Medicineinteraction;
import mk.ukim.finki.synergymed.models.MedicineinteractionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineinteractionRepository extends JpaRepository<Medicineinteraction, MedicineinteractionId> {
}