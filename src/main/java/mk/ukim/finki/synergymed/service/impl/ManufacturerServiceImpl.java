package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.Manufacturer;
import mk.ukim.finki.synergymed.repositories.CompanyRepository;
import mk.ukim.finki.synergymed.repositories.ManufacturerRepository;
import mk.ukim.finki.synergymed.service.ManufacturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final CompanyRepository companyRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository,
                                   CompanyRepository companyRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Optional<Manufacturer> findById(Integer companyId) {
        return manufacturerRepository.findById(companyId);
    }

    @Override
    @Transactional
    public Manufacturer create(Integer companyId) {
        if (manufacturerRepository.existsById(companyId)) {
            throw new IllegalStateException("Manufacturer already exists for company " + companyId);
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + companyId));
        Manufacturer m = new Manufacturer();
        m.setCompany(company); // @MapsId copies company.id into manufacturer.id
        return manufacturerRepository.save(m);
    }

    @Override
    @Transactional
    public void deleteById(Integer companyId) {
        manufacturerRepository.deleteById(companyId);
    }
}
