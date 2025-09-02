package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Brandedmedicineimage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BrandedMedicineService {
    List<Brandedmedicine> findAll();
    Optional<Brandedmedicine> findById(Integer id);

    void deleteById(Integer id) throws IOException;

    List<Brandedmedicineimage> listImages(Integer brandedMedicineId);
    String cardImageUrl(Integer brandedMedicineId);
    Map<Integer,String> cardImageUrlsFor(List<Brandedmedicine> medicines);

    void saveAll(Integer id,
            Integer manufacturerId,
            BigDecimal price,
            String description,
            String dosageForm,
            String strength,
            String originCountry,
            String name,
            MultipartFile[] newImages,
            List<Integer> removeImageIds,
            Integer mainExistingId,
            Integer mainNewIndex) throws IOException;
}
