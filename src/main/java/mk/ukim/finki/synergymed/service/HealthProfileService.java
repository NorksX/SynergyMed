package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Healthprofile;
import mk.ukim.finki.synergymed.models.User;

import java.time.LocalDate;
import java.util.Optional;

public interface HealthProfileService {
    Healthprofile createForClient(Client client, String bloodType);

    public void addAllergy(Integer clientId,
                           Integer medicineId,
                           LocalDate dateDiagnosed,
                           String description,
                           String severity);


    Optional<Healthprofile> getByClientId(Integer clientId);
}
