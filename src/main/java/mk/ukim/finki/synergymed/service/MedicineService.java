package mk.ukim.finki.synergymed.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mk.ukim.finki.synergymed.models.Medicine;

import java.io.IOException;
import java.util.List;

public interface MedicineService {

    List<Medicine> findAllMedicine();
    Medicine save(String medicineName,String activeIngredients) throws IOException;
}
