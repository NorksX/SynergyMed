package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.exceptions.HealthProfileDoesNotExistException;
import mk.ukim.finki.synergymed.exceptions.MedicineDoesNotExistException;
import mk.ukim.finki.synergymed.models.*;
import mk.ukim.finki.synergymed.repositories.AllergicreactionHealthprofileMedicineRepository;
import mk.ukim.finki.synergymed.repositories.HealthprofileRepository;
import mk.ukim.finki.synergymed.repositories.MedicineRepository;
import mk.ukim.finki.synergymed.service.HealthProfileService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthProfileServiceImpl implements HealthProfileService {

    private final HealthprofileRepository healthprofileRepository;
    private final AllergicreactionHealthprofileMedicineRepository allergicreactionHealthprofileMedicineRepo;
    private final MedicineRepository medicineRepository;

    @Override
    public Healthprofile createForClient(Client client, String bloodType) {

        Healthprofile profile = new Healthprofile();
        profile.setClient(client);
        profile.setBloodType(bloodType);

        return healthprofileRepository.save(profile);
    }

    @Override
    public void addAllergy(Integer clientId,
                           Integer medicineId,
                           LocalDate dateDiagnosed,
                           String description,
                           String severity) {

        Healthprofile healthprofile = healthprofileRepository
                .findByClientId(clientId)
                .orElseThrow(HealthProfileDoesNotExistException::new);

        Medicine medicine = medicineRepository
                .getMedicineById(medicineId)
                .orElseThrow(MedicineDoesNotExistException::new);

        AllergicreactionHealthprofileMedicine allergy = new AllergicreactionHealthprofileMedicine();

        AllergicreactionHealthprofileMedicineId key = new AllergicreactionHealthprofileMedicineId(
                healthprofile.getId(),
                medicineId
        );

        allergy.setId(key);
        allergy.setHealthProfile(healthprofile);
        allergy.setMedicine(medicine);
        allergy.setDateDiagnosed(dateDiagnosed);
        allergy.setDescription(description);
        allergy.setSeverity(severity);

        allergicreactionHealthprofileMedicineRepo.save(allergy);
    }


    @Override
    public Optional<Healthprofile> getByClientId(Integer clientId) {
        return healthprofileRepository.findByClientId(clientId);
    }
}
