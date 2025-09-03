// src/main/java/.../service/impl/CompanyOrchestrationServiceImpl.java
package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.enumerations.CompanyRoleType;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyOrchestrationServiceImpl implements CompanyOrchestrationService {

    private final CompanyService companyService;
    private final DistributorService distributorService;
    private final ManufacturerService manufacturerService;
    private final PharmacyService pharmacyService;
    private final DeliveryCompanyService deliveryCompanyService;

    @Override
    @Transactional
    public Company createCompanyWithRoles(String companyName, String description, String registrationNumber,
                                          List<CompanyRoleType> roles) {
        Company c = companyService.save(companyName, description, registrationNumber);
        for (CompanyRoleType r : roles) {
            try {
                switch (r) {
                    case DISTRIBUTOR      -> distributorService.create(c.getId());
                    case MANUFACTURER     -> manufacturerService.create(c.getId());
                    case PHARMACY         -> pharmacyService.create(c.getId());
                    case DELIVERY_COMPANY -> deliveryCompanyService.create(c.getId());
                }
            } catch (DataIntegrityViolationException ignore) {
                // idempotent add if another request just created the row under a unique/PK constraint
            }
        }
        return c;
    } // service-level transaction boundary covers the whole use case [1][6]

    @Override
    @Transactional
    public void updateCompanyAndRoles(Integer companyId, String companyName, String description,
                                      String registrationNumber, List<CompanyRoleType> requestedRoles) {

        companyService.update(companyId, companyName, description, registrationNumber);

        Set<CompanyRoleType> current = EnumSet.noneOf(CompanyRoleType.class);
        if (distributorService.findById(companyId).isPresent())      current.add(CompanyRoleType.DISTRIBUTOR);
        if (manufacturerService.findById(companyId).isPresent())     current.add(CompanyRoleType.MANUFACTURER);
        if (pharmacyService.findById(companyId).isPresent())         current.add(CompanyRoleType.PHARMACY);
        if (deliveryCompanyService.findById(companyId).isPresent())  current.add(CompanyRoleType.DELIVERY_COMPANY);

        Set<CompanyRoleType> requested = EnumSet.noneOf(CompanyRoleType.class);
        requested.addAll(requestedRoles);

        if (current.equals(requested)) return; // no-op on unchanged roles

        // Adds
        for (CompanyRoleType r : requested) {
            if (!current.contains(r)) {
                try {
                    switch (r) {
                        case DISTRIBUTOR      -> distributorService.create(companyId);
                        case MANUFACTURER     -> manufacturerService.create(companyId);
                        case PHARMACY         -> pharmacyService.create(companyId);
                        case DELIVERY_COMPANY -> deliveryCompanyService.create(companyId);
                    }
                } catch (DataIntegrityViolationException ignore) {
                    // safe duplicate under unique/PK
                }
            }
        }

        // Removes
        for (CompanyRoleType r : current) {
            if (!requested.contains(r)) {
                switch (r) {
                    case DISTRIBUTOR      -> distributorService.deleteById(companyId);
                    case MANUFACTURER     -> manufacturerService.deleteById(companyId);
                    case PHARMACY         -> pharmacyService.deleteById(companyId);
                    case DELIVERY_COMPANY -> deliveryCompanyService.deleteById(companyId);
                }
            }
        }
    }
}
