package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Healthprofile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthprofileRepository extends JpaRepository<Healthprofile, Integer> {
}