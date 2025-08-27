package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.PharmacyCatalog;
import mk.ukim.finki.synergymed.models.PharmacyCatalogId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyCatalogRepository extends JpaRepository<PharmacyCatalog, PharmacyCatalogId> {
}