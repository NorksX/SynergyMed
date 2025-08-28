package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {
    List<Manufacturer> findAll();
    Optional<Manufacturer> findById(Integer companyId);
    Manufacturer create(Integer companyId);
    void deleteById(Integer companyId);
}
