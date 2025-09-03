package mk.ukim.finki.synergymed.web;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Brandedmedicineimage;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import mk.ukim.finki.synergymed.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/branded-medicines")
public class BrandedMedicineController {

    private final BrandedMedicineService brandedMedicineService;
    private final ManufacturerService manufacturerService;

    public BrandedMedicineController(BrandedMedicineService brandedMedicineService,
                                     ManufacturerService manufacturerService) {
        this.brandedMedicineService = brandedMedicineService;
        this.manufacturerService = manufacturerService;
    }


    @GetMapping
    public String index(Model model) {
        List<Brandedmedicine> medicines = brandedMedicineService.findAll();
        Map<Integer, String> firstImageById = brandedMedicineService.cardImageUrlsFor(medicines);
        model.addAttribute("medicines", medicines);
        model.addAttribute("firstImageById", firstImageById);
        return "index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("mode", "create");
        return "branded-medicine-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Brandedmedicine bm = brandedMedicineService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + id));
        List<Brandedmedicineimage> images = brandedMedicineService.listImages(id);

        model.addAttribute("bm", bm);
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("images", images);
        model.addAttribute("mode", "edit");
        return "branded-medicine-form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Integer id,
            @RequestParam Integer manufacturerId,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description,
            @RequestParam String dosageForm,
            @RequestParam String strength,
            @RequestParam(required = false) String originCountry,
            @RequestParam String name,

            @RequestParam(name = "images", required = false) MultipartFile[] images,
            @RequestParam(name = "removeImageIds", required = false) List<Integer> removeImageIds,

            @RequestParam(name = "mainExistingId", required = false) String mainExistingIdStr,
            @RequestParam(name = "mainNewIndex",   required = false) String mainNewIndexStr
    ) throws IOException {

        Integer mainExistingId = (mainExistingIdStr != null && mainExistingIdStr.matches("^\\d+$"))
                ? Integer.valueOf(mainExistingIdStr) : null;
        Integer mainNewIndex   = (mainNewIndexStr   != null && mainNewIndexStr.matches("^\\d+$"))
                ? Integer.valueOf(mainNewIndexStr) : null;

        brandedMedicineService.saveAll(
                id, manufacturerId, price, description, dosageForm, strength, originCountry, name,
                images, removeImageIds, mainExistingId, mainNewIndex
        );
        return "redirect:/admin/branded-medicines";
    }


    @PostMapping("/{id}/delete")
    public String deleteBrandedMedicine(@PathVariable Integer id) throws IOException {
        brandedMedicineService.deleteById(id);
        return "redirect:/admin/branded-medicines";
    }
}
