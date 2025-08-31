// VerificationReviewServiceImpl.java
package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.repositories.SensitiveclientdataRepository;
import mk.ukim.finki.synergymed.service.VerificationReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationReviewServiceImpl implements VerificationReviewService {

    private final SensitiveclientdataRepository sensitiveRepo;
    private final ClientRepository clientRepo;

    @Override @Transactional(readOnly = true)
    public List<Sensitiveclientdata> listPending() {
        return sensitiveRepo.findByVerificationStatusOrderByIdAsc("во тек");
    }

    @Override @Transactional(readOnly = true)
    public Optional<Sensitiveclientdata> get(Integer id) {
        return sensitiveRepo.findById(id);
    }

    @Override
    public void approve(Integer id) {
        Sensitiveclientdata row = sensitiveRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        row.setVerificationStatus("одобрена");
        sensitiveRepo.save(row);
        Integer clientId = row.getClient().getId();
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        client.setIsVerified(Boolean.TRUE);
        clientRepo.save(client);
    }

    @Override
    public void deny(Integer id) {
        Sensitiveclientdata row = sensitiveRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        row.setVerificationStatus("одбиена");
        sensitiveRepo.save(row);
    }
}
