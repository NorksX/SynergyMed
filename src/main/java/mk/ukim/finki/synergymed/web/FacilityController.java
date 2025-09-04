package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Company;
import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.Contactinformation;
import mk.ukim.finki.synergymed.service.CompanyService;
import mk.ukim.finki.synergymed.service.FacilityService;
import mk.ukim.finki.synergymed.service.ContactInformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/companies/{companyId}/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final CompanyService companyService;
    private final FacilityService facilityService;
    private final ContactInformationService contactInformationService;

    @GetMapping
    public String index(@PathVariable Integer companyId, Model model) {
        Company company = companyService.findById(companyId).orElseThrow();
        List<Facility> facilities = facilityService.findAllByCompany(companyId);
        model.addAttribute("company", company);
        model.addAttribute("facilities", facilities);
        return "facilities";
    }

    @GetMapping("/new")
    public String createForm(@PathVariable Integer companyId, Model model) {
        Company company = companyService.findById(companyId).orElseThrow();
        model.addAttribute("company", company);
        model.addAttribute("mode", "create");
        return "facility-form";
    }

    @PostMapping
    public String create(@PathVariable Integer companyId,
                         @RequestParam String facilityName,
                         @RequestParam String code,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Facility saved = facilityService.create(companyId, facilityName, code);
        ra.addFlashAttribute("message", "Facility created: " + saved.getFacilityName());
        return "redirect:/admin/companies/" + companyId + "/facilities";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer companyId,
                           @PathVariable Integer id,
                           Model model) {
        Company company = companyService.findById(companyId).orElseThrow();
        Facility facility = facilityService.findById(id).orElseThrow();
        model.addAttribute("company", company);
        model.addAttribute("facility", facility);
        model.addAttribute("mode", "edit");
        return "facility-form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer companyId,
                         @PathVariable Integer id,
                         @RequestParam String facilityName,
                         @RequestParam String code,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Facility updated = facilityService.update(id, facilityName, code);
        ra.addFlashAttribute("message", "Facility updated: " + updated.getFacilityName());
        return "redirect:/admin/companies/" + companyId + "/facilities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer companyId,
                         @PathVariable Integer id,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        facilityService.delete(id);
        ra.addFlashAttribute("message", "Facility deleted");
        return "redirect:/admin/companies/" + companyId + "/facilities";
    }

    @GetMapping("/{id}/inventory")
    public String inventory(@PathVariable Integer companyId,
                            @PathVariable Integer id,
                            Model model) {
        var company = companyService.findById(companyId).orElseThrow();
        var facility = facilityService.findById(id).orElseThrow();
        var items = facilityService.listInventoryItems(id);
        model.addAttribute("company", company);
        model.addAttribute("facility", facility);
        model.addAttribute("items", items);
        return "facility-inventory";
    }

    @GetMapping("/{id}/contacts")
    public String facilityContacts(@PathVariable Integer companyId,
                                   @PathVariable Integer id,
                                   Model model) {
        var company = companyService.findById(companyId).orElseThrow();
        var facility = facilityService.findById(id).orElseThrow();
        var list = contactInformationService.listForFacility(id);
        Contactinformation contact = list.isEmpty() ? null : list.get(0);
        model.addAttribute("company", company);
        model.addAttribute("facility", facility);
        model.addAttribute("contact", contact);
        return "facility-contacts";
    }

    @GetMapping("/{id}/contacts/new")
    public String newFacilityContact(@PathVariable Integer companyId,
                                     @PathVariable Integer id,
                                     Model model) {
        var company = companyService.findById(companyId).orElseThrow();
        var facility = facilityService.findById(id).orElseThrow();
        model.addAttribute("company", company);
        model.addAttribute("facility", facility);
        model.addAttribute("context", "facility");
        model.addAttribute("postUrl", "/admin/companies/" + companyId + "/facilities/" + id + "/contacts/save");
        model.addAttribute("backUrl", "/admin/companies/" + companyId + "/facilities/" + id + "/contacts");
        return "contact-form";
    }

    @PostMapping("/{id}/contacts/save")
    public String saveFacilityContact(@PathVariable Integer companyId,
                                      @PathVariable Integer id,
                                      @RequestParam(required = false) Integer contactId,
                                      @RequestParam(required = false) String phone,
                                      @RequestParam(required = false) String address) {
        if (contactId == null) {
            contactInformationService.createForFacility(id, phone, address);
        } else {
            contactInformationService.updateForFacility(contactId, id, phone, address);
        }
        return "redirect:/admin/companies/" + companyId + "/facilities/" + id + "/contacts";
    }

    @PostMapping("/{id}/contacts/delete")
    public String deleteFacilityContact(@PathVariable Integer companyId,
                                        @PathVariable Integer id,
                                        @RequestParam Integer contactId) {
        contactInformationService.deleteForFacility(contactId, id);
        return "redirect:/admin/companies/" + companyId + "/facilities/" + id + "/contacts";
    }
}
