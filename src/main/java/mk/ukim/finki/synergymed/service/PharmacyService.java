package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Pharmacy;

import java.util.List;
import java.util.Optional;

public interface PharmacyService {
    List<Pharmacy> findAll();
    Optional<Pharmacy> findById(Integer companyId);
    Pharmacy create(Integer companyId);
    void deleteById(Integer companyId);
}
