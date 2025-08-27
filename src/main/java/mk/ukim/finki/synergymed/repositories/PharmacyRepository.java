package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer> {
}