package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> findAllClientsWithoutHealthProfile() {
        return clientRepository.findClientsWithoutHealthProfile();
    }

    @Override
    public List<Client> findClientsWithoutHealthProfileByName(String searchTerm) {
        String term = "%" + searchTerm.toLowerCase() + "%";
        return clientRepository.findClientsWithoutHealthProfileByName(term);
    }

    @Override
    public Client findClientById(Integer clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id " + clientId));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public boolean isVerified(Integer userId) {
        return clientRepository.findById(userId)
                .map(c -> Boolean.TRUE.equals(c.getIsVerified()))
                .orElse(false);
    }
}