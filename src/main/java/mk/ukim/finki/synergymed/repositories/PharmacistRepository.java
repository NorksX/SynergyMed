package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Integer> {
    boolean existsById(Integer userId);

    Optional<Pharmacist> findByUsersUsername(String username);

}