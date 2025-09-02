package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.Brandedmedicineimage;
import mk.ukim.finki.synergymed.models.Manufacturer;
import mk.ukim.finki.synergymed.repositories.BrandedmedicineRepository;
import mk.ukim.finki.synergymed.repositories.BrandedmedicineimageRepository;
import mk.ukim.finki.synergymed.repositories.ManufacturerRepository;
import mk.ukim.finki.synergymed.service.BrandedMedicineService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;

@Service
public class BrandedMedicineServiceImpl implements BrandedMedicineService {

    private final BrandedmedicineRepository brandedMedicineRepository;
    private final BrandedmedicineimageRepository brandedMedicineImageRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Value("${app.upload.branded-medicine-dir:uploads/images/branded_medicine/}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS =
            Arrays.asList("jpg","jpeg","png","gif","webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public BrandedMedicineServiceImpl(BrandedmedicineRepository brandedMedicineRepository,
                                      BrandedmedicineimageRepository brandedMedicineImageRepository,
                                      ManufacturerRepository manufacturerRepository) {
        this.brandedMedicineRepository = brandedMedicineRepository;
        this.brandedMedicineImageRepository = brandedMedicineImageRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Brandedmedicine> findAll() {
        return brandedMedicineRepository.findAll();
    }

    @Override
    public Optional<Brandedmedicine> findById(Integer id) {
        return brandedMedicineRepository.findById(id);
    }

    @Override
    public List<Brandedmedicineimage> listImages(Integer brandedMedicineId) {
        return brandedMedicineImageRepository.findByBrandedMedicineIdOrderByIdAsc(brandedMedicineId);
    }

    @Override
    public String cardImageUrl(Integer brandedMedicineId) {
        return brandedMedicineImageRepository.findFirstByBrandedMedicineIdAndMainImageTrue(brandedMedicineId)
                .map(Brandedmedicineimage::getImage)
                .orElseGet(() -> brandedMedicineImageRepository
                        .findFirstByBrandedMedicineIdOrderByIdAsc(brandedMedicineId)
                        .map(Brandedmedicineimage::getImage)
                        .orElse("/images/placeholder.png"));
    }

    @Override
    public Map<Integer, String> cardImageUrlsFor(List<Brandedmedicine> medicines) {
        Map<Integer,String> map = new HashMap<>();
        for (Brandedmedicine bm : medicines) {
            String url = cardImageUrl(bm.getId());
            if (!url.startsWith("/")) url = "/" + url;
            map.put(bm.getId(), url);
        }
        return map;
    }

    @Override
    @Transactional(
            rollbackFor = { Exception.class, java.io.IOException.class },
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            timeout = 30
    )
    public void saveAll(Integer id,
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
                        Integer mainNewIndex) throws IOException {

        Brandedmedicine bm;
        if (id == null) {
            bm = new Brandedmedicine();
        } else {
            bm = brandedMedicineRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + id));
        }

        Manufacturer m = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found: " + manufacturerId));
        bm.setManufacturer(m);
        bm.setPrice(price);
        bm.setDescription(description);
        bm.setDosageForm(dosageForm);
        bm.setStrength(strength);
        bm.setOriginCountry(originCountry);
        bm.setName(name);

        Brandedmedicine saved = brandedMedicineRepository.save(bm);

        if (removeImageIds != null && !removeImageIds.isEmpty()) {
            List<Brandedmedicineimage> toRemove = brandedMedicineImageRepository.findAllById(removeImageIds);
            for (Brandedmedicineimage img : toRemove) {
                if (!Objects.equals(img.getBrandedMedicine().getId(), saved.getId())) continue;
                deletePhysicalFileIfExists(img.getImage());
            }
            brandedMedicineImageRepository.deleteAll(toRemove);
        }

        List<Brandedmedicineimage> appended = new ArrayList<>();
        if (newImages != null) {
            Path base = ensureUploadPath();
            long ts = System.currentTimeMillis();
            int seq = 0;
            for (MultipartFile file : newImages) {
                if (file == null || file.isEmpty()) continue;
                validateImageFile(file);

                String original = Optional.ofNullable(file.getOriginalFilename()).orElse("image");
                String ext = getFileExtension(original).toLowerCase(Locale.ROOT);
                String filename = String.format("branded_medicine_%d_%d_%03d.%s", saved.getId(), ts, ++seq, ext);

                Path dest = base.resolve(filename);
                Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

                String url = "/uploads/images/branded_medicine/" + filename;

                Brandedmedicineimage img = new Brandedmedicineimage();
                img.setBrandedMedicine(saved);
                img.setImage(url);
                img.setMainImage(false);
                appended.add(brandedMedicineImageRepository.save(img));
            }
        }

        Integer targetMainId = null;

        if (mainExistingId != null) {
            brandedMedicineImageRepository.findById(mainExistingId).ifPresent(img -> {
                if (Objects.equals(img.getBrandedMedicine().getId(), saved.getId())) {
                    // capture via array holder
                }
            });
            if (brandedMedicineImageRepository.findById(mainExistingId)
                    .filter(img -> Objects.equals(img.getBrandedMedicine().getId(), saved.getId()))
                    .isPresent()) {
                targetMainId = mainExistingId;
            }
        }

        if (targetMainId == null && mainNewIndex != null) {
            if (mainNewIndex >= 0 && mainNewIndex < appended.size()) {
                targetMainId = appended.get(mainNewIndex).getId();
            }
        }

        if (targetMainId == null) {
            Optional<Brandedmedicineimage> curMain = brandedMedicineImageRepository
                    .findFirstByBrandedMedicineIdAndMainImageTrue(saved.getId());
            if (curMain.isPresent()) {
                targetMainId = curMain.get().getId();
            } else {
                targetMainId = brandedMedicineImageRepository
                        .findFirstByBrandedMedicineIdOrderByIdAsc(saved.getId())
                        .map(Brandedmedicineimage::getId).orElse(null);
            }
        }

        if (targetMainId != null) {
            List<Brandedmedicineimage> all = brandedMedicineImageRepository.findByBrandedMedicineId(saved.getId());
            for (Brandedmedicineimage img : all) {
                boolean shouldBeMain = Objects.equals(img.getId(), targetMainId);
                if (img.isMainImage() != shouldBeMain) {
                    img.setMainImage(shouldBeMain);
                    brandedMedicineImageRepository.save(img);
                }
            }
        }
    }
    @Override
    @Transactional
    public void deleteById(Integer id) throws IOException {
        List<Brandedmedicineimage> imgs = brandedMedicineImageRepository.findByBrandedMedicineId(id);
        for (Brandedmedicineimage img : imgs) {
            deletePhysicalFileIfExists(img.getImage());
        }

        brandedMedicineImageRepository.deleteAll(imgs);

        brandedMedicineRepository.deleteById(id);
    }


    private Path ensureUploadPath() throws IOException {
        Path p = Paths.get(uploadDir);
        if (!Files.exists(p)) Files.createDirectories(p);
        return p;
    }


    private void deletePhysicalFileIfExists(String storedUrl) throws IOException {
        if (storedUrl == null || storedUrl.isBlank()) return;
        String fileName = Paths.get(storedUrl).getFileName().toString();
        Path onDisk = Paths.get(uploadDir).resolve(fileName);
        if (Files.exists(onDisk)) Files.delete(onDisk);
    }

    private void validateImageFile(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) throw new IOException("File exceeds 5MB");
        String name = file.getOriginalFilename();
        if (name == null || name.isEmpty()) throw new IOException("Invalid filename");
        String ext = getFileExtension(name).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(ext)) throw new IOException("Not an allowed image type");
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) throw new IOException("Not an image");
    }

    private String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return i == -1 ? "" : filename.substring(i + 1);
    }
}
