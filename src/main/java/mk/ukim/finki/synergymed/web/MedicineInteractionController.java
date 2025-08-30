package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.Medicineinteraction;
import mk.ukim.finki.synergymed.service.MedicineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medicine/interactions")
@RequiredArgsConstructor
public class MedicineInteractionController {

    private final MedicineService medicineService;

    @GetMapping
    public String getInteractions(Model model) {
        List<Medicine> medicineList = medicineService.findAll();
        model.addAttribute("medicineList", medicineList);

        return "medicine-interactions";
    }

    @PostMapping("/search")
    public String searchInteractions(@RequestParam String searchTerm,
                                     Model model) {

        List<Medicine> medicineList = medicineService.findAll();
        model.addAttribute("medicineList", medicineList);
        try {
            List<Medicineinteraction> interactions = medicineService.interactions(searchTerm);
            model.addAttribute("interactions", interactions);

        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }
        return "medicine-interactions";
    }

    @GetMapping("/{id}")
    public String getInteractionsForMedicine(@PathVariable Integer id,
                                             Model model) {

        List<Medicine> medicineList = medicineService.findAll();
        model.addAttribute("medicineList", medicineList);
        try {
            Optional<Medicine> optMedicine = medicineService.findById(id);
            if (optMedicine.isEmpty()) {
                throw new RuntimeException("Medicine not found");
            }

            Medicine medicine = optMedicine.get();
            List<Medicineinteraction> interactions = medicineService.interactions(medicine.getMedicineName());
            model.addAttribute("interactions", interactions);
            model.addAttribute("searchedMedicine", medicine.getMedicineName());

            if (interactions.isEmpty()) {
                model.addAttribute("hasInfo", true);
                model.addAttribute("info", "No interactions found for " + medicine.getMedicineName());
            }

        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }

        return "medicine-interactions";
    }

    @GetMapping("/add")
    public String getAddInteractionsPage(Model model) {
        model.addAttribute("medicineList", medicineService.findAll());
        return "add-medicine-interaction";
    }

    @PostMapping("/add")
    public String addInteractions(@RequestParam Integer medicine1Id,
                                  @RequestParam Integer medicine2Id,
                                  @RequestParam String type,
                                  @RequestParam String severity,
                                  @RequestParam String description,
                                  Model model) {
        try {
            if (medicine1Id.equals(medicine2Id)) {
                throw new RuntimeException("You cannot select the same medicine twice.");
            }

            Optional<Medicine> med1 = medicineService.findById(medicine1Id);
            Optional<Medicine> med2 = medicineService.findById(medicine2Id);

            if (med1.isEmpty() || med2.isEmpty()) {
                throw new RuntimeException("The selected medicine does not exist.");
            }

            medicineService.addInteraction(med1.get(), med2.get(), type, description, severity);

            model.addAttribute("hasSuccess", true);
            model.addAttribute("success", "Interaction successfully added.");
        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }

        model.addAttribute("medicineList", medicineService.findAll());
        return "add-medicine-interaction";
    }
}