package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}