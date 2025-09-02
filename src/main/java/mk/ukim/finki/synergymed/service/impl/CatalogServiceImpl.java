package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Pharmacy;
import mk.ukim.finki.synergymed.models.PharmacyCatalog;
import mk.ukim.finki.synergymed.models.PharmacyCatalogId;
import mk.ukim.finki.synergymed.repositories.BrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.PharmacyCatalogRepository;
import mk.ukim.finki.synergymed.repositories.PharmacyRepository;
import mk.ukim.finki.synergymed.service.CatalogService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final PharmacyCatalogRepository catalogRepo;
    private final BrandedmedicineRepository bmRepo;
    private final PharmacyRepository pharmacyRepo;

    @Override
    public List<Brandedmedicine> listCatalogMedicines(Integer pharmacyId) {
        return catalogRepo.findAllByPharmacyIdWithMedicine(pharmacyId)
                .stream()
                .map(PharmacyCatalog::getBrandedMedicine)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Integer> listCatalogMedicineIds(Integer pharmacyId) {
        return new HashSet<>(catalogRepo.findAllMedicineIdsInCatalog(pharmacyId));
    }

    @Override
    public void setCatalog(Integer pharmacyId, Collection<Integer> brandedMedicineIds) {
        Set<Integer> desired = brandedMedicineIds == null ? Set.of() : new HashSet<>(brandedMedicineIds);

        // Current
        Set<Integer> current = listCatalogMedicineIds(pharmacyId);

        // To add and to remove
        Set<Integer> toAdd = new HashSet<>(desired);
        toAdd.removeAll(current);

        Set<Integer> toRemove = new HashSet<>(current);
        toRemove.removeAll(desired);

        // Add
        if (!toAdd.isEmpty()) {
            Pharmacy pharmacy = pharmacyRepo.findById(pharmacyId)
                    .orElseThrow(() -> new IllegalArgumentException("Pharmacy not found: " + pharmacyId));
            List<Brandedmedicine> addEntities = bmRepo.findAllById(toAdd);
            List<PharmacyCatalog> newLinks = new ArrayList<>(addEntities.size());
            for (Brandedmedicine bm : addEntities) {
                PharmacyCatalog pc = new PharmacyCatalog();
                pc.setId(new PharmacyCatalogId(pharmacy.getId(), bm.getId()));
                pc.setPharmacy(pharmacy);
                pc.setBrandedMedicine(bm);
                newLinks.add(pc);
            }
            catalogRepo.saveAll(newLinks);
        }

        // Remove
        for (Integer bmId : toRemove) {
            catalogRepo.deleteByPharmacy_IdAndBrandedMedicine_Id(pharmacyId, bmId);
        }
    }
}
