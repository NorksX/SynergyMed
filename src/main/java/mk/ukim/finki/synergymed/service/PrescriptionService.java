package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Prescription;

import java.util.List;

public interface PrescriptionService {
    List<Prescription> listForClient(Integer clientId);
}
