package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Distributor;
import mk.ukim.finki.synergymed.models.Facility;
import mk.ukim.finki.synergymed.models.Inventory;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicine;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicineId;
import mk.ukim.finki.synergymed.models.Pharmacy;
import mk.ukim.finki.synergymed.models.Supplyorder;
import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicine;
import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicineId;
import mk.ukim.finki.synergymed.repositories.BrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.DistributorBrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.DistributorRepository;
import mk.ukim.finki.synergymed.repositories.FacilityRepository;
import mk.ukim.finki.synergymed.repositories.InventoryBrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.InventoryRepository;
import mk.ukim.finki.synergymed.repositories.PharmacyRepository;
import mk.ukim.finki.synergymed.repositories.SupplyorderBrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.SupplyorderRepository;
import mk.ukim.finki.synergymed.service.SupplyOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SupplyOrderServiceImpl implements SupplyOrderService {

    private final SupplyorderRepository supplyorderRepository;
    private final SupplyorderBrandedmedicineRepository sobmRepository;
    private final PharmacyRepository pharmacyRepository;
    private final FacilityRepository facilityRepository;
    private final InventoryRepository inventoryRepository;
    private final BrandedmedicineRepository brandedmedicineRepository;
    private final DistributorBrandedmedicineRepository distributorBrandedmedicineRepository;
    private final DistributorRepository distributorRepository;
    private final InventoryBrandedmedicineRepository inventoryBrandedmedicineRepository;

    @Override
    @Transactional
    public Integer createSupplyOrder(Integer pharmacyId,
                                     Integer facilityId,
                                     Integer distributorId,
                                     Integer[] medicineIds,
                                     Integer[] quantities) {

        if (medicineIds == null || quantities == null || medicineIds.length == 0 || medicineIds.length != quantities.length) {
            throw new IllegalArgumentException("Invalid medicine/quantity arrays");
        }

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy not found: " + pharmacyId));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found: " + facilityId));
        Distributor distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new EntityNotFoundException("Distributor not found: " + distributorId));

        Map<Integer, Integer> qtyByBm = new LinkedHashMap<>();
        for (int i = 0; i < medicineIds.length; i++) {
            Integer bmId = medicineIds[i];
            Integer qty = Optional.ofNullable(quantities[i]).orElse(0);
            if (bmId == null || qty == null || qty <= 0) continue;
            qtyByBm.merge(bmId, qty, Integer::sum);
        }
        if (qtyByBm.isEmpty()) {
            throw new IllegalArgumentException("No positive quantities provided");
        }

        List<Integer> allowed = distributorBrandedmedicineRepository.existingForDistributor(distributorId, qtyByBm.keySet());
        if (allowed.size() != qtyByBm.keySet().size()) {
            throw new IllegalArgumentException("Some medicines are not provided by the selected distributor");
        }

        LocalDate now = LocalDate.now();
        Supplyorder order = new Supplyorder();
        order.setPharmacy(pharmacy);
        order.setFacility(facility);
        order.setDistributor(distributor);
        order.setOrderDate(now);
        order.setExpectedArrivalDate(now.plusDays(7));
        order.setStatus("во тек");
        order = supplyorderRepository.save(order);

        Inventory inventory = inventoryRepository.findByFacilityId(facilityId)
                .orElseGet(() -> {
                    Inventory inv = new Inventory();
                    inv.setFacility(facility);
                    return inventoryRepository.save(inv);
                });

        for (Map.Entry<Integer, Integer> e : qtyByBm.entrySet()) {
            Integer bmId = e.getKey();
            Integer qty = e.getValue();

            Brandedmedicine bm = brandedmedicineRepository.findById(bmId)
                    .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + bmId));

            SupplyorderBrandedmedicine sobm = new SupplyorderBrandedmedicine();
            SupplyorderBrandedmedicineId sobmId = new SupplyorderBrandedmedicineId();
            sobmId.setSupplyOrderId(order.getId());
            sobmId.setBrandedMedicineId(bmId);
            sobm.setId(sobmId);
            sobm.setSupplyOrder(order);
            sobm.setBrandedMedicine(bm);
            sobm.setQuantity(qty);
            sobmRepository.save(sobm);

            InventoryBrandedmedicineId invId = new InventoryBrandedmedicineId();
            invId.setInventoryId(inventory.getId());
            invId.setBrandedMedicineId(bmId);

            InventoryBrandedmedicine ibm = inventoryBrandedmedicineRepository
                    .findById(invId)
                    .orElseGet(() -> {
                        InventoryBrandedmedicine x = new InventoryBrandedmedicine();
                        x.setId(invId);
                        x.setInventory(inventory);
                        x.setBrandedMedicine(bm);
                        x.setQuantity(0);
                        x.setLastChanged(now);
                        return x;
                    });

            ibm.setQuantity(ibm.getQuantity() + qty);
            ibm.setLastChanged(now);

            inventoryBrandedmedicineRepository.save(ibm);
        }

        return order.getId();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Supplyorder> listAll() {
        return supplyorderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Supplyorder getById(Integer id) {
        return supplyorderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supply order not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplyorderBrandedmedicine> linesFor(Integer orderId) {
        return sobmRepository.findAllBySupplyOrderIdFetchMedicine(orderId);
    }
}

