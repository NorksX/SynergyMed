package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}