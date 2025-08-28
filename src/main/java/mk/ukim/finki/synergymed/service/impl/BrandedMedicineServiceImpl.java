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
    private String uploadDir; // write to ./uploads/images/branded_medicine/

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
    public List<Brandedmedicine> findAll() { return brandedMedicineRepository.findAll(); }

    @Override
    public Optional<Brandedmedicine> findById(Integer id) { return brandedMedicineRepository.findById(id); }

    @Override
    @Transactional
    public Brandedmedicine create(Integer manufacturerId, BigDecimal price, String description,
                                  String dosageForm, String strength, String originCountry,
                                  String name, MultipartFile[] images) throws IOException {
        Manufacturer m = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found: " + manufacturerId));
        Brandedmedicine bm = new Brandedmedicine();
        bm.setManufacturer(m);
        bm.setPrice(price);
        bm.setDescription(description);
        bm.setDosageForm(dosageForm);
        bm.setStrength(strength);
        bm.setOriginCountry(originCountry);
        bm.setName(name);

        Brandedmedicine saved = brandedMedicineRepository.save(bm);
        if (images != null && images.length > 0) {
            processImageUploads(images, saved);
        }
        return saved;
    }

    @Override
    @Transactional
    public Brandedmedicine update(Integer id, Integer manufacturerId, BigDecimal price, String description,
                                  String dosageForm, String strength, String originCountry,
                                  String name, MultipartFile[] images,
                                  boolean replaceImagesIgnored) throws IOException {
        Brandedmedicine bm = brandedMedicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + id));

        if (manufacturerId != null) {
            Manufacturer m = manufacturerRepository.findById(manufacturerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found: " + manufacturerId));
            bm.setManufacturer(m);
        }
        if (price != null) bm.setPrice(price);
        bm.setDescription(description);
        bm.setDosageForm(dosageForm);
        bm.setStrength(strength);
        bm.setOriginCountry(originCountry);
        bm.setName(name);

        Brandedmedicine saved = brandedMedicineRepository.save(bm);
        if (images != null && images.length > 0) {
            processImageUploads(images, saved); // append only
        }
        return saved;
    }

    @Override
    @Transactional
    public void addImages(Integer brandedMedicineId, MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) return;
        Brandedmedicine bm = brandedMedicineRepository.findById(brandedMedicineId)
                .orElseThrow(() -> new EntityNotFoundException("Branded medicine not found: " + brandedMedicineId));
        processImageUploads(images, bm);
    }

    @Override
    @Transactional
    public void deleteImage(Integer imageId) throws IOException {
        Brandedmedicineimage img = brandedMedicineImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Branded medicine image not found: " + imageId));

        Integer bmId = img.getBrandedMedicine().getId();
        boolean wasMain = img.isMainImage();

        deletePhysicalFileIfExists(img.getImage());
        brandedMedicineImageRepository.deleteById(imageId);

        if (wasMain) {
            brandedMedicineImageRepository.findFirstByBrandedMedicineIdOrderByIdAsc(bmId)
                    .ifPresent(next -> {
                        next.setMainImage(true);
                        brandedMedicineImageRepository.save(next);
                    });
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws IOException {
        List<Brandedmedicineimage> imgs = brandedMedicineImageRepository.findByBrandedMedicineId(id);
        for (Brandedmedicineimage img : imgs) deletePhysicalFileIfExists(img.getImage());
        brandedMedicineImageRepository.deleteAll(imgs);
        brandedMedicineRepository.deleteById(id);
    }

    // New: list images for edit page
    @Override
    public List<Brandedmedicineimage> listImages(Integer brandedMedicineId) {
        return brandedMedicineImageRepository.findByBrandedMedicineIdOrderByIdAsc(brandedMedicineId);
    }

    // New: resolve card image URL (main or fallback, else placeholder)
    @Override
    public String cardImageUrl(Integer brandedMedicineId) {
        return brandedMedicineImageRepository.findFirstByBrandedMedicineIdAndMainImageTrue(brandedMedicineId)
                .map(Brandedmedicineimage::getImage)
                .orElseGet(() -> brandedMedicineImageRepository
                        .findFirstByBrandedMedicineIdOrderByIdAsc(brandedMedicineId)
                        .map(Brandedmedicineimage::getImage)
                        .orElse("/images/placeholder.png"));
    }

    // New: convenience map for list pages
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
    @Transactional
    public void setMainImage(Integer brandedMedicineId, Integer imageId) {
        Brandedmedicineimage chosen = brandedMedicineImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));
        if (!Objects.equals(chosen.getBrandedMedicine().getId(), brandedMedicineId)) {
            throw new IllegalArgumentException("Image does not belong to this Brandedmedicine");
        }
        List<Brandedmedicineimage> all = brandedMedicineImageRepository.findByBrandedMedicineId(brandedMedicineId);
        for (Brandedmedicineimage img : all) {
            boolean shouldBeMain = Objects.equals(img.getId(), imageId);
            if (img.isMainImage() != shouldBeMain) {
                img.setMainImage(shouldBeMain);
                brandedMedicineImageRepository.save(img);
            }
        }
    }

    // ---------- helpers ----------

    private void processImageUploads(MultipartFile[] images, Brandedmedicine bm) throws IOException {
        Path base = ensureUploadPath();
        long ts = System.currentTimeMillis();
        int i = 0;

        boolean hasMain = brandedMedicineImageRepository.existsByBrandedMedicineIdAndMainImageTrue(bm.getId());

        for (MultipartFile file : images) {
            if (file == null || file.isEmpty()) continue;
            validateImageFile(file);

            String original = Optional.ofNullable(file.getOriginalFilename()).orElse("image");
            String ext = getFileExtension(original).toLowerCase(Locale.ROOT);
            String filename = String.format("branded_medicine_%d_%d_%03d.%s", bm.getId(), ts, ++i, ext);

            Path dest = base.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/images/branded_medicine/" + filename;

            Brandedmedicineimage img = new Brandedmedicineimage();
            img.setBrandedMedicine(bm);
            img.setImage(url);
            img.setMainImage(!hasMain && i == 1); // first image if no main yet
            brandedMedicineImageRepository.save(img);
        }
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
