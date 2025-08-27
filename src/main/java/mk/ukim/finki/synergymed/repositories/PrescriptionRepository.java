package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
}