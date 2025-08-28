package mk.ukim.finki.synergymed.service;


import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clubcard;

import java.util.Optional;

public interface ClubCardService {
    Clubcard createForClient(Client client);
    Optional<Clubcard> getByClientId(Integer clientId);
}
