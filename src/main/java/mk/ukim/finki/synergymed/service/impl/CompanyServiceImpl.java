package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.repositories.CompanyRepository;
import mk.ukim.finki.synergymed.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(Integer id) {
        return companyRepository.findById(id);
    }

    @Override
    @Transactional
    public Company save(String companyName, String description, String registrationNumber) {
        Company c = new Company();
        c.setCompanyName(companyName);
        c.setDescription(description);
        c.setRegistrationNumber(registrationNumber);
        return companyRepository.save(c);
    }

    @Override
    @Transactional
    public Company update(Integer id, String companyName, String description, String registrationNumber) {
        Company c = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + id));
        c.setCompanyName(companyName);
        c.setDescription(description);
        c.setRegistrationNumber(registrationNumber);
        return companyRepository.save(c);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        companyRepository.deleteById(id);
    }
}
