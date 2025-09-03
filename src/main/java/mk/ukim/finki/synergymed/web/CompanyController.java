package mk.ukim.finki.synergymed.web;

import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.enumerations.CompanyRoleType;
import mk.ukim.finki.synergymed.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyOrchestrationService companyOrchestrationService;
    private final DistributorService distributorService;
    private final ManufacturerService manufacturerService;
    private final PharmacyService pharmacyService;
    private final DeliveryCompanyService deliveryCompanyService;

    public CompanyController(CompanyService companyService,
                             CompanyOrchestrationService companyOrchestrationService,
                             DistributorService distributorService,
                             ManufacturerService manufacturerService,
                             PharmacyService pharmacyService,
                             DeliveryCompanyService deliveryCompanyService) {
        this.companyService = companyService;
        this.companyOrchestrationService = companyOrchestrationService;
        this.distributorService = distributorService;
        this.manufacturerService = manufacturerService;
        this.pharmacyService = pharmacyService;
        this.deliveryCompanyService = deliveryCompanyService;
    }

    // Grid of companies
    @GetMapping
    public String index(Model model) {
        List<Company> companies = companyService.findAll();
        Map<Integer, List<CompanyRoleType>> rolesByCompany = new HashMap<>();
        for (Company c : companies) {
            List<CompanyRoleType> roles = new ArrayList<>();
            if (distributorService.findById(c.getId()).isPresent()) roles.add(CompanyRoleType.DISTRIBUTOR);
            if (manufacturerService.findById(c.getId()).isPresent()) roles.add(CompanyRoleType.MANUFACTURER);
            if (pharmacyService.findById(c.getId()).isPresent()) roles.add(CompanyRoleType.PHARMACY);
            try {
                if (deliveryCompanyService.findById(c.getId()).isPresent()) {
                    roles.add(CompanyRoleType.valueOf("DELIVERYCOMPANY"));
                }
            } catch (IllegalArgumentException ignored) {}
            rolesByCompany.put(c.getId(), roles);
        }
        model.addAttribute("companies", companies);
        model.addAttribute("rolesByCompany", rolesByCompany);
        return "companies";
    }

    // Create form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("roleTypes", CompanyRoleType.values()); // checkboxes shown only on create
        model.addAttribute("mode", "create");
        return "company-form";
    }

    // Create with roles
    @PostMapping
    public String create(@RequestParam String companyName,
                         @RequestParam(required = false) String description,
                         @RequestParam String registrationNumber,
                         @RequestParam(name = "roles", required = false) List<CompanyRoleType> roles,
                         RedirectAttributes ra) {
        Company saved = companyOrchestrationService.createCompanyWithRoles(
                companyName, description, registrationNumber, roles == null ? List.of() : roles
        );
        ra.addFlashAttribute("message", "Company created: " + saved.getCompanyName());
        return "redirect:/admin/companies";
    }

    // GET /companies/{id}/edit — pre-check roles using current DB state
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Company c = companyService.findById(id).orElseThrow();
        Set<CompanyRoleType> selectedRoles = new HashSet<>();
        if (distributorService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.DISTRIBUTOR);
        if (manufacturerService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.MANUFACTURER);
        if (pharmacyService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.PHARMACY);
        try { if (deliveryCompanyService.findById(id).isPresent()) selectedRoles.add(CompanyRoleType.valueOf("DELIVERYCOMPANY")); }
        catch (IllegalArgumentException ignored) {}

        model.addAttribute("company", c);
        model.addAttribute("roleTypes", CompanyRoleType.values());
        model.addAttribute("selectedRoles", selectedRoles);
        model.addAttribute("mode", "edit");
        return "company-form";
    }

    // POST /companies/{id}/update — simple update + role reset
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @RequestParam String companyName,
                         @RequestParam(required = false) String description,
                         @RequestParam String registrationNumber,
                         @RequestParam(name = "roles", required = false) List<CompanyRoleType> roles,
                         RedirectAttributes ra) {
        // 1) Update core fields (your existing simple update)
        companyService.update(id, companyName, description, registrationNumber);

        // 2) Reset roles: remove all, then add only the submitted ones
        distributorService.deleteById(id);
        manufacturerService.deleteById(id);
        pharmacyService.deleteById(id);
        try { deliveryCompanyService.deleteById(id); } catch (Exception ignored) {}

        if (roles != null) {
            for (CompanyRoleType r : roles) {
                switch (r) {
                    case DISTRIBUTOR -> distributorService.create(id);
                    case MANUFACTURER -> manufacturerService.create(id);
                    case PHARMACY -> pharmacyService.create(id);
                    default -> {
                        if ("DELIVERYCOMPANY".equals(r.name())) {
                            deliveryCompanyService.create(id);
                        }
                    }
                }
            }
        }

        ra.addFlashAttribute("message", "Company updated: " + companyName);
        return "redirect:/admin/companies";
    }


    // Delete (FKs should cascade from company to roles in DB)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        companyService.deleteById(id);
        ra.addFlashAttribute("message", "Company deleted");
        return "redirect:/admin/companies";
    }
}
