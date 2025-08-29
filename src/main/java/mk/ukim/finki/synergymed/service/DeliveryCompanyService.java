package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Deliverycompany;
import mk.ukim.finki.synergymed.models.Distributor;

import java.util.List;
import java.util.Optional;

public interface DeliveryCompanyService {
    List<Deliverycompany> findAll();
    Optional<Deliverycompany> findById(Integer companyId);
    Deliverycompany create(Integer companyId);
    void deleteById(Integer companyId);
}
