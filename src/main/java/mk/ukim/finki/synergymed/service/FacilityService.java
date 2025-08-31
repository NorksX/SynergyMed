package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicine;

import java.util.List;
import java.util.Optional;

public interface FacilityService {
    Optional<Facility> findById(Integer id);
    List<Facility> findAllByCompany(Integer companyId);
    List<Facility> findAll();
    Facility create(Integer companyId, String facilityName, String code);
    Facility update(Integer id, String facilityName, String code);
    void delete(Integer id);

    List<InventoryBrandedmedicine> listInventoryItems(Integer facilityId);

}