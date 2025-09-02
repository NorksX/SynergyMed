package mk.ukim.finki.synergymed.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Prescription;
import mk.ukim.finki.synergymed.repositories.PrescriptionRepository;
import mk.ukim.finki.synergymed.service.PrescriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Override
    public List<Prescription> listForClient(Integer clientId) {
        return prescriptionRepository.findByClient_IdOrderByIssuedAtDesc(clientId);
    }
}