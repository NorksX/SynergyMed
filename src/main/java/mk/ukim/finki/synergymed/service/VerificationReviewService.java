package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Sensitiveclientdata;

import java.util.List;
import java.util.Optional;

public interface VerificationReviewService {
    List<Sensitiveclientdata> listPending();
    Optional<Sensitiveclientdata> get(Integer id);
    void approve(Integer id);
    void deny(Integer id);
}