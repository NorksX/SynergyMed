package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
}