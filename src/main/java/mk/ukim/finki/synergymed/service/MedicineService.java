package mk.ukim.finki.synergymed.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mk.ukim.finki.synergymed.models.Medicine;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MedicineService {
    Optional<Medicine> findById(Integer id);

    List<Medicine> findAll();
    Medicine save(String medicineName,String activeIngredients);

    Medicine update(Integer id, String medicineName, String activeIngredients);

    void deleteById(Integer id);
}
