package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.Distributor;
import mk.ukim.finki.synergymed.repositories.CompanyRepository;
import mk.ukim.finki.synergymed.repositories.DistributorRepository;
import mk.ukim.finki.synergymed.service.DistributorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final DistributorRepository distributorRepository;
    private final CompanyRepository companyRepository;

    public DistributorServiceImpl(DistributorRepository distributorRepository,
                                  CompanyRepository companyRepository) {
        this.distributorRepository = distributorRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Distributor> findAll() {
        return distributorRepository.findAll();
    }

    @Override
    public Optional<Distributor> findById(Integer companyId) {
        return distributorRepository.findById(companyId);
    }

    @Override
    @Transactional
    public Distributor create(Integer companyId) {
        if (distributorRepository.existsById(companyId)) {
            throw new IllegalStateException("Distributor already exists for company " + companyId);
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + companyId));
        Distributor d = new Distributor();
        d.setCompany(company);
        return distributorRepository.save(d);
    }

    @Override
    @Transactional
    public void deleteById(Integer companyId) {
        distributorRepository.deleteById(companyId);
    }
}
