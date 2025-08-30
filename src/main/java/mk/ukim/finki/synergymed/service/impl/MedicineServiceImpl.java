package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.exceptions.MedicineDoesNotExistException;
import mk.ukim.finki.synergymed.exceptions.MedicineInteractionAlreadyExistsException;
import mk.ukim.finki.synergymed.models.Medicine;
import mk.ukim.finki.synergymed.models.Medicineinteraction;
import mk.ukim.finki.synergymed.models.MedicineinteractionId;
import mk.ukim.finki.synergymed.repositories.MedicineRepository;
import mk.ukim.finki.synergymed.repositories.MedicineinteractionRepository;
import mk.ukim.finki.synergymed.service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineinteractionRepository medicineinteractionRepository;


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

    @Override
    public Medicineinteraction addInteraction(Medicine medicine1,
                                              Medicine medicine2,
                                              String type,
                                              String description,
                                              String severity) {
        boolean exists = medicineinteractionRepository
                .findById_MedicineId1AndId_MedicineId2(medicine1.getId(), medicine2.getId())
                .isPresent()
                || medicineinteractionRepository
                .findById_MedicineId1AndId_MedicineId2(medicine2.getId(), medicine1.getId())
                .isPresent();

        if (exists) {
            throw new MedicineInteractionAlreadyExistsException();
        }

        MedicineinteractionId id = new MedicineinteractionId();
        id.setMedicineId1(medicine1.getId());
        id.setMedicineId2(medicine2.getId());

        Medicineinteraction interaction = new Medicineinteraction();
        interaction.setId(id);
        interaction.setMedicineId1(medicine1);
        interaction.setMedicineId2(medicine2);
        interaction.setType(type);
        interaction.setDescription(description);
        interaction.setSeverity(severity);

        return medicineinteractionRepository.save(interaction);
    }

    @Override
    public List<Medicineinteraction> interactions(String medicineName) {
        Optional<Medicine> medicine = medicineRepository.getMedicineByMedicineName(medicineName);

        if (medicine.isEmpty()) {
            throw new MedicineDoesNotExistException();
        }

        Integer medId = medicine.get().getId();

        List<Medicineinteraction> interactionsAsFirst = medicineinteractionRepository.findById_MedicineId1(medId);
        List<Medicineinteraction> interactionsAsSecond = medicineinteractionRepository.findById_MedicineId2(medId);

        List<Medicineinteraction> allInteractions = new ArrayList<>();
        allInteractions.addAll(interactionsAsFirst);
        allInteractions.addAll(interactionsAsSecond);

        return allInteractions;
    }

}
