package mk.ukim.finki.synergymed.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Brandedmedicineimage;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import mk.ukim.finki.synergymed.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping({"/","catalog"})
public class CatalogController {

    private final CatalogService catalogService;
    private final BrandedMedicineService brandedMedicineService;



    @GetMapping
    public String catalog(Model model, HttpSession session) {
        var listed = catalogService.listCatalogMedicines(3);
        var firstImageById = brandedMedicineService.cardImageUrlsFor(listed);
        model.addAttribute("medicines", listed);
        model.addAttribute("firstImageById", firstImageById);
        model.addAttribute("username", session.getAttribute("username"));
        return "catalog";
    }

    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @GetMapping("/edit")
    public String edit(Model model, HttpSession session) {
        var all = brandedMedicineService.findAll();
        var selectedIds = catalogService.listCatalogMedicineIds(3);
        model.addAttribute("medicines", all);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("username", session.getAttribute("username"));
        return "catalog-edit";
    }
    @GetMapping("details/{id}")
    public String details(@PathVariable Integer id, Model model) {
        Brandedmedicine bm = brandedMedicineService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + id));
        List<Brandedmedicineimage> images = brandedMedicineService.listImages(id);
        model.addAttribute("bm", bm);
        model.addAttribute("images", images);
        return "branded-medicine-details";
    }

    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @PostMapping("/edit")
    public String saveEdit(@RequestParam(name="ids", required=false) List<Integer> ids) {
        catalogService.setCatalog(3, ids);
        return "redirect:/catalog";
    }
}
