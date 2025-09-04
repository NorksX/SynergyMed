// src/main/java/.../web/CompanyController.java
package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.enumerations.CompanyRoleType;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyOrchestrationService companyOrchestrationService;
    private final DistributorService distributorService;
    private final ManufacturerService manufacturerService;
    private final PharmacyService pharmacyService;
    private final DeliveryCompanyService deliveryCompanyService;

    @GetMapping
    public String index(Model model) {
        List<Company> companies = companyService.findAll();
        Map<Integer, List<CompanyRoleType>> rolesByCompany = new HashMap<>();
        for (Company c : companies) {
            Integer id = c.getId();
            List<CompanyRoleType> roles = new ArrayList<>();
            if (distributorService.findById(id).isPresent())  roles.add(CompanyRoleType.DISTRIBUTOR);
            if (manufacturerService.findById(id).isPresent()) roles.add(CompanyRoleType.MANUFACTURER);
            if (pharmacyService.findById(id).isPresent())     roles.add(CompanyRoleType.PHARMACY);
            if (deliveryCompanyService.findById(id).isPresent()) roles.add(CompanyRoleType.DELIVERY_COMPANY);
            rolesByCompany.put(id, roles);
        }
        model.addAttribute("companies", companies);
        model.addAttribute("rolesByCompany", rolesByCompany);
        return "companies";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("roleTypes", CompanyRoleType.values());
        model.addAttribute("mode", "create");
        return "company-form";
    }

    @PostMapping
    public String create(@RequestParam String companyName,
                         @RequestParam(required = false) String description,
                         @RequestParam String registrationNumber,
                         @RequestParam(name = "roles", required = false) List<CompanyRoleType> roles) {
        companyOrchestrationService.createCompanyWithRoles(
                companyName, description, registrationNumber, roles == null ? List.of() : roles
        );
        return "redirect:/admin/companies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Company c = companyService.findById(id).orElseThrow();
        Set<CompanyRoleType> selectedRoles = new HashSet<>();
        if (distributorService.findById(id).isPresent())  selectedRoles.add(CompanyRoleType.DISTRIBUTOR);
        if (manufacturerService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.MANUFACTURER);
        if (pharmacyService.findById(id).isPresent())     selectedRoles.add(CompanyRoleType.PHARMACY);
        if (deliveryCompanyService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.DELIVERY_COMPANY);

        model.addAttribute("company", c);
        model.addAttribute("roleTypes", CompanyRoleType.values());
        model.addAttribute("selectedRoles", selectedRoles);
        model.addAttribute("mode", "edit");
        return "company-form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @RequestParam String companyName,
                         @RequestParam(required = false) String description,
                         @RequestParam String registrationNumber,
                         @RequestParam(name = "roles", required = false) List<CompanyRoleType> roles) {
        companyOrchestrationService.updateCompanyAndRoles(
                id, companyName, description, registrationNumber, roles == null ? List.of() : roles
        );
        return "redirect:/admin/companies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        companyService.deleteById(id);
        return "redirect:/admin/companies";
    }
}
