package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByFacilityId(Integer facilityId);
    void deleteByFacilityId(Integer facilityId);
}