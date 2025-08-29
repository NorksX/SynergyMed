package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.Medicineinteraction;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    Optional<Medicine> findById(Integer id);

    List<Medicine> findAll();
    Medicine save(String medicineName,String activeIngredients);

//    Medicine findById(Integer id);
//    Medicine save(String medicineName,String activeIngredients) throws InvalidInputException, MedicineAlreadyExistsException;
    List<Medicineinteraction> interactions(String medicineName);
    Medicineinteraction addInteraction(Medicine medicine1, Medicine medicine2, String type, String description, String severity);
    Medicine update(Integer id, String medicineName, String activeIngredients);

    void deleteById(Integer id);
}
