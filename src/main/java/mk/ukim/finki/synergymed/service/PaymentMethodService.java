package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Paymentmethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    List<Paymentmethod> findAll();
    Optional<Paymentmethod> findById(Integer id);
}
