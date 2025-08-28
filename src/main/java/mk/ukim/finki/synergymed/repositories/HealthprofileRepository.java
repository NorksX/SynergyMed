package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Healthprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthprofileRepository extends JpaRepository<Healthprofile, Integer> {
    Optional<Healthprofile> findByClientId(Integer client_id);
}