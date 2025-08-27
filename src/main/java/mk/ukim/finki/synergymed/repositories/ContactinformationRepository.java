package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Contactinformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactinformationRepository extends JpaRepository<Contactinformation, Integer> {
}