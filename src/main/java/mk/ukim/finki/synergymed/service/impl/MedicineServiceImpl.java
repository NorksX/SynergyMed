package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.repositories.MedicineRepository;
import mk.ukim.finki.synergymed.service.MedicineService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<Medicine> findAll() {
        return this.medicineRepository.findAll();
    }

    @Override
    public Optional<Medicine> findById(Integer id) {
        return this.medicineRepository.findById(id);
    }

    @Override
    public Medicine save(String medicineName, String activeIngredients){
        Medicine medicine = new Medicine();
        medicine.setMedicineName(medicineName);
        medicine.setActiveIngredient(activeIngredients);
        return this.medicineRepository.save(medicine);
    }

    @Override
    public Medicine update(Integer id, String medicineName, String activeIngredients) {
        Medicine medicine = this.medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found: " + id));

        medicine.setMedicineName(medicineName);
        medicine.setActiveIngredient(activeIngredients);

        return this.medicineRepository.save(medicine);
    }

    @Override
    public void deleteById(Integer id) {
        this.medicineRepository.deleteById(id);
    }

}
