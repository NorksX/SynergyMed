// src/main/java/mk/ukim/finki/synergymed/web/SupplyOrderController.java
package mk.ukim.finki.synergymed.web;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Distributor;
import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.Pharmacy;
import mk.ukim.finki.synergymed.repositories.*;
import mk.ukim.finki.synergymed.service.SupplyOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pharmacist/supply-orders")
public class SupplyOrderController {

    private final SupplyOrderService supplyOrderService;
    private final PharmacyRepository pharmacyRepository;
    private final FacilityRepository facilityRepository;
    private final DistributorRepository distributorRepository;
    private final DistributorBrandedmedicineRepository distributorBrandedmedicineRepository;

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        List<Distributor> distributors = distributorRepository.findAll();

        // Preload facilities per pharmacy (by pharmacy.company.id)
        Map<Integer, List<Facility>> facilitiesByPharmacyId = new HashMap<>();
        for (Pharmacy p : pharmacies) {
            if (p.getCompany() == null) continue;
            var facs = facilityRepository.findAllByCompanyId(p.getCompany().getId());
            facilitiesByPharmacyId.put(p.getId(), facs);
        }

        // Preload medicines per distributor
        Map<Integer, List<Brandedmedicine>> medicinesByDistributorId = new HashMap<>();
        for (Distributor d : distributors) {
            var meds = distributorBrandedmedicineRepository.findMedicinesByDistributor(d.getId());
            medicinesByDistributorId.put(d.getId(), meds);
        }

        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("distributors", distributors);
        model.addAttribute("facilitiesByPharmacyId", facilitiesByPharmacyId);
        model.addAttribute("medicinesByDistributorId", medicinesByDistributorId);
        return "supplyorder"; // Thymeleaf template below
    }

    @PostMapping
    public String create(@RequestParam Integer pharmacyId,
                         @RequestParam Integer facilityId,
                         @RequestParam Integer distributorId,
                         @RequestParam("medicineIds") Integer[] medicineIds,
                         @RequestParam("quantities") Integer[] quantities) {
        Integer id = supplyOrderService.createSupplyOrder(
                pharmacyId, facilityId, distributorId, medicineIds, quantities
        );
        return "redirect:/pharmacist/supply-orders/" + id;
    }
        @GetMapping
        public String list(Model model) {
            model.addAttribute("orders", supplyOrderService.listAll());
            return "supplyorder-list";
        }

        @GetMapping("/{id}")
        public String details(@PathVariable Integer id, Model model) {
            model.addAttribute("order", supplyOrderService.getById(id));
            model.addAttribute("lines", supplyOrderService.linesFor(id));
            return "supplyorder-details";
        }
    }
