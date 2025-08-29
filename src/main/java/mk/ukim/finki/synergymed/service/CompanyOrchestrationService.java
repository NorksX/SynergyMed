package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.enumerations.CompanyRoleType;

import java.util.List;

public interface CompanyOrchestrationService {
    Company createCompanyWithRoles(
            String companyName,
            String description,
            String registrationNumber,
            List<CompanyRoleType> roles
    );
}
