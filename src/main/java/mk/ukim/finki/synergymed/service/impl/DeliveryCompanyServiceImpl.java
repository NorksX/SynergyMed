package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.Deliverycompany;
import mk.ukim.finki.synergymed.repositories.CompanyRepository;
import mk.ukim.finki.synergymed.repositories.DeliverycompanyRepository;
import mk.ukim.finki.synergymed.service.DeliveryCompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryCompanyServiceImpl implements DeliveryCompanyService {

    private final DeliverycompanyRepository deliveryCompanyRepository;
    private final CompanyRepository companyRepository;

    public DeliveryCompanyServiceImpl(DeliverycompanyRepository deliveryCompanyRepository,
                                      CompanyRepository companyRepository) {
        this.deliveryCompanyRepository = deliveryCompanyRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Deliverycompany> findAll() {
        return deliveryCompanyRepository.findAll();
    }

    @Override
    public Optional<Deliverycompany> findById(Integer companyId) {
        return deliveryCompanyRepository.findById(companyId);
    }

    @Override
    @Transactional
    public Deliverycompany create(Integer companyId) {
        if (deliveryCompanyRepository.existsById(companyId)) {
            throw new IllegalStateException("Delivery company already exists for company " + companyId);
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + companyId));

        Deliverycompany dc = new Deliverycompany();
        dc.setCompany(company);

        return deliveryCompanyRepository.save(dc);
    }

    @Override
    @Transactional
    public void deleteById(Integer companyId) {
        deliveryCompanyRepository.deleteById(companyId);
    }
}
