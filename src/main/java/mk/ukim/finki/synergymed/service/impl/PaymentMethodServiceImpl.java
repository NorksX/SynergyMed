package mk.ukim.finki.synergymed.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Paymentmethod;
import mk.ukim.finki.synergymed.repositories.PaymentmethodRepository;
import mk.ukim.finki.synergymed.service.PaymentMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentmethodRepository paymentmethodRepository;

    @Override
    public List<Paymentmethod> findAll() {
        return paymentmethodRepository
                .findAll();
    }

    @Override
    public Optional<Paymentmethod> findById(Integer id) {
        return paymentmethodRepository
                .findById(id);
    }
}
