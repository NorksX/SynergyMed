package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Clientorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientorderRepository extends JpaRepository<Clientorder, Integer> {
}