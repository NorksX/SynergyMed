package mk.ukim.finki.synergymed.service;


import mk.ukim.finki.synergymed.models.Supplyorder;
import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicine;

import java.util.List;

public interface SupplyOrderService {
    Integer createSupplyOrder(Integer pharmacyId,
                              Integer facilityId,
                              Integer distributorId,
                              Integer[] medicineIds,
                              Integer[] quantities);

    List<Supplyorder> listAll();
    Supplyorder getById(Integer id);
    List<SupplyorderBrandedmedicine> linesFor(Integer orderId);
}