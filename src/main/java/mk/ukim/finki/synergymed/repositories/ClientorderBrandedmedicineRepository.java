package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.ClientorderBrandedmedicine;
import mk.ukim.finki.synergymed.models.ClientorderBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientorderBrandedmedicineRepository extends JpaRepository<ClientorderBrandedmedicine, ClientorderBrandedmedicineId> {
}