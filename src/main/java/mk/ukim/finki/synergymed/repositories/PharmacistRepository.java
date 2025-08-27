package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Integer> {
}