package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.Pharmacy;
import mk.ukim.finki.synergymed.repositories.CompanyRepository;
import mk.ukim.finki.synergymed.repositories.PharmacyRepository;
import mk.ukim.finki.synergymed.service.PharmacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final CompanyRepository companyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository,
                               CompanyRepository companyRepository) {
        this.pharmacyRepository = pharmacyRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Optional<Pharmacy> findById(Integer companyId) {
        return pharmacyRepository.findById(companyId);
    }

    @Override
    @Transactional
    public Pharmacy create(Integer companyId) {
        if (pharmacyRepository.existsById(companyId)) {
            throw new IllegalStateException("Pharmacy already exists for company " + companyId);
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + companyId));
        Pharmacy p = new Pharmacy();
        p.setCompany(company);
        return pharmacyRepository.save(p);
    }

    @Override
    @Transactional
    public void deleteById(Integer companyId) {
        pharmacyRepository.deleteById(companyId);
    }
}
