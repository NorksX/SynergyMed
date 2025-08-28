package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAll();
    Optional<Company> findById(Integer id);
    Company save(String companyName, String description, String registrationNumber);
    Company update(Integer id, String companyName, String description, String registrationNumber);
    void deleteById(Integer id);
}