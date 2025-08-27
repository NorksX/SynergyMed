package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
}