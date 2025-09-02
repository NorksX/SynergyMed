package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.Inventory;
import mk.ukim.finki.synergymed.repositories.InventoryRepository;
import mk.ukim.finki.synergymed.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    @Override
    public Optional<Inventory> findByFacilityId(Integer facilityId) {
        return inventoryRepository.findByFacilityId(facilityId);
    }

    @Override
    public Inventory createFor(Facility facility) {
        return inventoryRepository.findByFacilityId(facility.getId())
                .orElseGet(() -> {
                    Inventory inv = new Inventory();
                    inv.setFacility(facility);
                    return inventoryRepository.save(inv);
                });
    }

    @Override
    public void deleteForFacility(Integer facilityId) {
        inventoryRepository.deleteByFacilityId(facilityId);
    }
}
