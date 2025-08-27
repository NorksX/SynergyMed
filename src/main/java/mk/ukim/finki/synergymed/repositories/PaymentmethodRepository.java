package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Paymentmethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentmethodRepository extends JpaRepository<Paymentmethod, Integer> {
}