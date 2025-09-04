package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import mk.ukim.finki.synergymed.models.*;
import mk.ukim.finki.synergymed.repositories.FacilityRepository;
import mk.ukim.finki.synergymed.repositories.InventoryBrandedmedicineRepository;
import mk.ukim.finki.synergymed.service.CompanyService;
import mk.ukim.finki.synergymed.service.FacilityService;
import mk.ukim.finki.synergymed.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;

    private final InventoryBrandedmedicineRepository inventoryBrandedmedicineRepository;

    private final CompanyService companyService;
    private final InventoryService inventoryService;


    public FacilityServiceImpl(FacilityRepository facilityRepository,
                               CompanyService companyService,
                               InventoryService inventoryService,
                               InventoryBrandedmedicineRepository inventoryBrandedmedicineRepository) {
        this.facilityRepository = facilityRepository;
        this.companyService = companyService;
        this.inventoryService = inventoryService;
        this.inventoryBrandedmedicineRepository = inventoryBrandedmedicineRepository;
    }

    @Override
    public Optional<Facility> findById(Integer id) {
        return facilityRepository.findById(id);
    }

    @Override
    public List<Facility> findAllByCompany(Integer companyId) {
        return facilityRepository.findAllByCompanyId(companyId);
    }

    @Override
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    @Transactional
    @Override
    public Facility create(Integer companyId, String facilityName, String code) {
        Company company = companyService.findById(companyId).orElseThrow();
        Facility f = new Facility();
        f.setCompany(company);
        f.setFacilityName(facilityName);
        f.setCode(code);
        Facility saved = facilityRepository.save(f);
        inventoryService.createFor(saved);
        return saved;
    }

    @Transactional
    @Override
    public Facility update(Integer id, String facilityName, String code) {
        Facility f = facilityRepository.findById(id).orElseThrow();
        f.setFacilityName(facilityName);
        f.setCode(code);
        return facilityRepository.save(f);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        inventoryService.deleteForFacility(id);
        facilityRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<InventoryBrandedmedicine> listInventoryItems(Integer facilityId) {
        Inventory inv = inventoryService.findByFacilityId(facilityId).orElseThrow();
        return inventoryBrandedmedicineRepository.findAllWithMedicineByInventoryId(inv.getId());
    }
}