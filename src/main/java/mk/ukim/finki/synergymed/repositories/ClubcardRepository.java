package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Clubcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubcardRepository extends JpaRepository<Clubcard, Integer> {
    Optional<Clubcard> findByUser_Id(Integer user_id);
}