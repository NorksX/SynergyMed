package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Brandedmedicineimage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandedmedicineimageRepository extends JpaRepository<Brandedmedicineimage, Integer> {

    // List all images for a branded medicine
    List<Brandedmedicineimage> findByBrandedMedicineId(Integer brandedMedicineId);

    // Same, ordered by id
    List<Brandedmedicineimage> findByBrandedMedicineIdOrderByIdAsc(Integer brandedMedicineId);

    // First image by id (fallback thumbnail)
    Optional<Brandedmedicineimage> findFirstByBrandedMedicineIdOrderByIdAsc(Integer brandedMedicineId);

    // “Main” image for a branded medicine (thumbnail of choice)
    Optional<Brandedmedicineimage> findFirstByBrandedMedicineIdAndMainImageTrue(Integer brandedMedicineId);

    // Existence of any “main” image for a branded medicine
    boolean existsByBrandedMedicineIdAndMainImageTrue(Integer brandedMedicineId);
}
