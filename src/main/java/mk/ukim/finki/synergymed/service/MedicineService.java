package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.exceptions.InvalidInputException;
import mk.ukim.finki.synergymed.exceptions.MedicineAlreadyExistsException;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.Medicineinteraction;

import java.util.List;

public interface MedicineService {

    List<Medicine> findAllMedicine();
    Medicine findById(Integer id);
    Medicine save(String medicineName,String activeIngredients) throws InvalidInputException, MedicineAlreadyExistsException;
    List<Medicineinteraction> interactions(String medicineName);
    Medicineinteraction addInteraction(Medicine medicine1, Medicine medicine2, String type, String description, String severity);
}
