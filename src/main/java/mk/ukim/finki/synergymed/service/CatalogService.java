package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Brandedmedicine;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CatalogService {
    // Read
    List<Brandedmedicine> listCatalogMedicines(Integer pharmacyId);
    Set<Integer> listCatalogMedicineIds(Integer pharmacyId);

    // Write (replace the catalog with exactly these ids)
    void setCatalog(Integer pharmacyId, Collection<Integer> brandedMedicineIds);
}
