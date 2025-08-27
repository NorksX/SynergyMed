package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Supplyorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyorderRepository extends JpaRepository<Supplyorder, Integer> {
}