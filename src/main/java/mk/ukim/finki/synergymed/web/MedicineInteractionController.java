package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.Medicineinteraction;
import mk.ukim.finki.synergymed.service.MedicineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/medicine/interactions")
@RequiredArgsConstructor
public class MedicineInteractionController {

    private final MedicineService medicineService;

    @GetMapping
    public String getInteractions(Model model){
        List<Medicine> medicineList = medicineService.findAllMedicine();
        model.addAttribute("medicineList", medicineList);

        return "medicine-interactions";
    }

    @PostMapping("/search")
    public String searchInteractions(@RequestParam String searchTerm,
                                     Model model){

        List<Medicine> medicineList = medicineService.findAllMedicine();
        model.addAttribute("medicineList", medicineList);
        try{
            List<Medicineinteraction> interactions = medicineService.interactions(searchTerm);
            model.addAttribute("interactions", interactions);

        }catch (RuntimeException ex){
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }
        return "medicine-interactions";
    }

    @GetMapping("/{id}")
    public String getInteractionsForMedicine(@PathVariable Integer id,
                                             Model model){

        List<Medicine> medicineList = medicineService.findAllMedicine();
        model.addAttribute("medicineList", medicineList);
        try{
            Medicine medicine = medicineService.findById(id);
            List<Medicineinteraction> interactions = medicineService.interactions(medicine.getMedicineName());
            model.addAttribute("interactions", interactions);
            model.addAttribute("searchedMedicine", medicine.getMedicineName());

            if(interactions.isEmpty()) {
                model.addAttribute("hasInfo", true);
                model.addAttribute("info", "No interactions found for " + medicine.getMedicineName());
            }

        }catch (RuntimeException ex){
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }

        return "medicine-interactions";
    }

    @GetMapping("/add")
    public String getAddInteractionsPage(Model model) {
        model.addAttribute("medicineList", medicineService.findAllMedicine());
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
                throw new RuntimeException("Не може да изберете ист лек два пати.");
            }

            Medicine med1 = medicineService.findById(medicine1Id);
            Medicine med2 = medicineService.findById(medicine2Id);

            medicineService.addInteraction(med1, med2, type, description, severity);

            model.addAttribute("hasSuccess", true);
            model.addAttribute("success", "Интеракцијата е успешно додадена.");
        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
        }

        model.addAttribute("medicineList", medicineService.findAllMedicine());
        return "add-medicine-interaction";
    }
}