package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByClient_IdOrderByIssuedAtDesc(Integer clientId);
}