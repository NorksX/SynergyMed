package mk.ukim.finki.synergymed.service.impl;

import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.enumerations.CompanyRoleType;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyOrchestrationServiceImpl implements CompanyOrchestrationService {

    private final CompanyService companyService;
    private final DistributorService distributorService;
    private final ManufacturerService manufacturerService;
    private final PharmacyService pharmacyService;

    public  CompanyOrchestrationServiceImpl(CompanyService companyService,
                                    DistributorService distributorService,
                                    ManufacturerService manufacturerService,
                                    PharmacyService pharmacyService) {
        this.companyService = companyService;
        this.distributorService = distributorService;
        this.manufacturerService = manufacturerService;
        this.pharmacyService = pharmacyService;
    }

    @Override
    @Transactional
    public Company createCompanyWithRoles(
            String companyName,
            String description,
            String registrationNumber,
            List<CompanyRoleType> roles
    ) {
        Company company = companyService.save(companyName, description, registrationNumber);
        if (roles != null) {
            for (CompanyRoleType role : roles) {
                switch (role) {
                    case DISTRIBUTOR -> distributorService.create(company.getId());
                    case MANUFACTURER -> manufacturerService.create(company.getId());
                    case PHARMACY -> pharmacyService.create(company.getId());
                }
            }
        }
        return company;
    }
}
