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

    Brandedmedicine create(Integer manufacturerId,
                           BigDecimal price,
                           String description,
                           String dosageForm,
                           String strength,
                           String originCountry,
                           String name,
                           MultipartFile[] images) throws IOException;

    Brandedmedicine update(Integer id,
                           Integer manufacturerId,
                           BigDecimal price,
                           String description,
                           String dosageForm,
                           String strength,
                           String originCountry,
                           String name,
                           MultipartFile[] images,
                           boolean replaceImagesIgnored) throws IOException;

    void addImages(Integer brandedMedicineId, MultipartFile[] images) throws IOException;
    void deleteImage(Integer imageId) throws IOException;
    void deleteById(Integer id) throws IOException;

    // New service methods so controllers don’t call repositories
    List<Brandedmedicineimage> listImages(Integer brandedMedicineId);
    String cardImageUrl(Integer brandedMedicineId); // main image or fallback
    Map<Integer,String> cardImageUrlsFor(List<Brandedmedicine> medicines);

    void setMainImage(Integer brandedMedicineId, Integer imageId);
}
