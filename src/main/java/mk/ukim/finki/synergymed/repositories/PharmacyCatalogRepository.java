package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.PharmacyCatalog;
import mk.ukim.finki.synergymed.models.PharmacyCatalogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PharmacyCatalogRepository extends JpaRepository<PharmacyCatalog, PharmacyCatalogId> {
    @Query("select pc from PharmacyCatalog pc join fetch pc.brandedMedicine bm where pc.pharmacy.id = :pharmacyId")
    List<PharmacyCatalog> findAllByPharmacyIdWithMedicine(@Param("pharmacyId") Integer pharmacyId);

    @Query("select pc.id.brandedMedicineId from PharmacyCatalog pc where pc.pharmacy.id = :pharmacyId")
    List<Integer> findAllMedicineIdsInCatalog(@Param("pharmacyId") Integer pharmacyId);

    boolean existsByPharmacy_IdAndBrandedMedicine_Id(Integer pharmacyId, Integer brandedMedicineId);

    void deleteByPharmacy_IdAndBrandedMedicine_Id(Integer pharmacyId, Integer brandedMedicineId);
}