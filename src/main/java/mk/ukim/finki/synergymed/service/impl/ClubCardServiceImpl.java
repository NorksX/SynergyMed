package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Clubcard;
import mk.ukim.finki.synergymed.repositories.ClubcardRepository;
import mk.ukim.finki.synergymed.service.ClubCardService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubCardServiceImpl implements ClubCardService {
    private final ClubcardRepository clubcardRepository;

    public Clubcard createForClient(Client client) {
        Clubcard card = new Clubcard();
        card.setUser(client);
        card.setClubProgram("Default Loyalty Program");
        card.setPoints(0);
        return clubcardRepository.save(card);
    }
    @Override
    public Optional<Clubcard> getByClientId(Integer clientId) {
        return clubcardRepository.findByUser_Id(clientId);
    }
}

