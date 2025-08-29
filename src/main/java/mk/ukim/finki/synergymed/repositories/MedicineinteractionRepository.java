package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Medicineinteraction;
import mk.ukim.finki.synergymed.models.MedicineinteractionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineinteractionRepository extends JpaRepository<Medicineinteraction, MedicineinteractionId> {

    List<Medicineinteraction> findById_MedicineId1(Integer medicineId1);
    List<Medicineinteraction> findById_MedicineId2(Integer medicineId2);

    Optional<Medicineinteraction> findById_MedicineId1AndId_MedicineId2(Integer id1, Integer id2);
}