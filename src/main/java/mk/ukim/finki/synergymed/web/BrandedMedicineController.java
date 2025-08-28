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
import java.util.*;

@Controller
@RequestMapping("/")
public class BrandedMedicineController {

    private final BrandedMedicineService brandedMedicineService;
    private final ManufacturerService manufacturerService;

    public BrandedMedicineController(BrandedMedicineService brandedMedicineService,
                                     ManufacturerService manufacturerService) {
        this.brandedMedicineService = brandedMedicineService;
        this.manufacturerService = manufacturerService;
    }

    // Home – list all branded medicines
    @GetMapping
    public String index(Model model) {
        List<Brandedmedicine> medicines = brandedMedicineService.findAll();
        Map<Integer, String> firstImageById = brandedMedicineService.cardImageUrlsFor(medicines);
        model.addAttribute("medicines", medicines);
        model.addAttribute("firstImageById", firstImageById);
        return "index";
    }

    // Create form
    @GetMapping("/branded-medicines/new")
    public String createForm(Model model) {
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("mode", "create");
        return "branded-medicine-form";
    }

    // Create: saves fields, appends any images, then goes home
    @PostMapping("/branded-medicines")
    public String create(@RequestParam Integer manufacturerId,
                         @RequestParam BigDecimal price,
                         @RequestParam(required = false) String description,
                         @RequestParam String dosageForm,
                         @RequestParam String strength,
                         @RequestParam(required = false) String originCountry,
                         @RequestParam String name,
                         @RequestParam(name = "images", required = false) MultipartFile[] images) throws IOException {

        brandedMedicineService.create(
                manufacturerId, price, description, dosageForm, strength, originCountry, name, images
        );
        return "redirect:/";
    }

    // Edit form (still available if needed)
    @GetMapping("/branded-medicines/{id}/edit")
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

    // Update: saves fields, appends any images, then goes home
    @PostMapping("/branded-medicines/{id}/update")
    public String update(@PathVariable Integer id,
                         @RequestParam Integer manufacturerId,
                         @RequestParam BigDecimal price,
                         @RequestParam(required = false) String description,
                         @RequestParam String dosageForm,
                         @RequestParam String strength,
                         @RequestParam(required = false) String originCountry,
                         @RequestParam String name,
                         @RequestParam(name = "images", required = false) MultipartFile[] images) throws IOException {

        // Service update already appends images if provided
        brandedMedicineService.update(
                id, manufacturerId, price, description, dosageForm, strength, originCountry, name, images, false
        );
        return "redirect:/";
    }

    // Delete branded medicine (+ all images)
    @PostMapping("/branded-medicines/{id}/delete")
    public String deleteBrandedMedicine(@PathVariable Integer id) throws IOException {
        brandedMedicineService.deleteById(id);
        return "redirect:/";
    }

    // Per-image actions (optional to keep; still useful if visiting the edit page)
    @PostMapping("/branded-medicines/{bmId}/images/{imageId}/delete")
    public String deleteImage(@PathVariable Integer bmId, @PathVariable Integer imageId) throws IOException {
        brandedMedicineService.deleteImage(imageId);
        return "redirect:/branded-medicines/" + bmId + "/edit";
    }

    @PostMapping("/branded-medicines/{bmId}/images/{imageId}/set-main")
    public String setMain(@PathVariable Integer bmId, @PathVariable Integer imageId) {
        brandedMedicineService.setMainImage(bmId, imageId);
        return "redirect:/branded-medicines/" + bmId + "/edit";
    }
}
