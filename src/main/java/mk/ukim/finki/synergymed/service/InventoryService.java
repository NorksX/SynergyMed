package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.Inventory;

import java.util.List;
import java.util.Optional;


public interface InventoryService {
    Optional<Inventory> findByFacilityId(Integer facilityId);
    Inventory createFor(Facility facility);
    void deleteForFacility(Integer facilityId);
}