package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Client;
import java.util.List;

public interface ClientService {

    List<Client> findAllClientsWithoutHealthProfile();

    List<Client> findClientsWithoutHealthProfileByName(String searchTerm);
    Client findClientById(Integer clientId);
    List<Client> findAll();
}