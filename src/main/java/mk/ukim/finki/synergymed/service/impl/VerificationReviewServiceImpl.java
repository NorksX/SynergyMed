package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import mk.ukim.finki.synergymed.models.Pharmacist;
import mk.ukim.finki.synergymed.repositories.SensitiveclientdataRepository;
import mk.ukim.finki.synergymed.repositories.PharmacistRepository;
import mk.ukim.finki.synergymed.service.VerificationReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationReviewServiceImpl implements VerificationReviewService {

    private final SensitiveclientdataRepository scdRepo;
    private final PharmacistRepository pharmacistRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Sensitiveclientdata> listPending() {
        return scdRepo.findByVerificationStatusOrderByIdAsc("во тек");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sensitiveclientdata> get(Integer id) {
        return scdRepo.findById(id);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        Sensitiveclientdata row = scdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        Pharmacist reviewer = getCurrentPharmacist();
        row.setPharmacist(reviewer);
        row.setVerificationStatus("верифицирано");
        scdRepo.save(row);
    }

    @Override
    @Transactional
    public void deny(Integer id) {
        Sensitiveclientdata row = scdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        Pharmacist reviewer = getCurrentPharmacist();
        row.setPharmacist(reviewer);
        row.setVerificationStatus("одбиено");
        scdRepo.save(row);
    }

    private Pharmacist getCurrentPharmacist() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return pharmacistRepo.findByUsersUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacist not found for user " + username));
    }
}
