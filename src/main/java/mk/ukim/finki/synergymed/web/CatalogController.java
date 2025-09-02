package mk.ukim.finki.synergymed.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import mk.ukim.finki.synergymed.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/catalog")
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

    @GetMapping("/edit")
    public String edit(Model model, HttpSession session) {
        var all = brandedMedicineService.findAll();
        var selectedIds = catalogService.listCatalogMedicineIds(3);
        model.addAttribute("medicines", all);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("username", session.getAttribute("username"));
        return "catalog-edit";
    }

    @PostMapping("/edit")
    public String saveEdit(@RequestParam(name="ids", required=false) java.util.List<Integer> ids) {
        catalogService.setCatalog(3, ids);
        return "redirect:/catalog";
    }
}
