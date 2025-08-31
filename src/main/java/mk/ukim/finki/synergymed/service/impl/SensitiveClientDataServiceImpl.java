package mk.ukim.finki.synergymed.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.Client;
import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import mk.ukim.finki.synergymed.repositories.SensitiveclientdataRepository;
import mk.ukim.finki.synergymed.service.SensitiveClientDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SensitiveClientDataServiceImpl implements SensitiveClientDataService {

    private final SensitiveclientdataRepository repo;
    private final ClientRepository clientRepo;

    @Value("${app.upload.portraits-dir:uploads/images/portraits/}")
    private String uploadDir;

    private static final String[] ALLOWED = {"jpg","jpeg","png","webp","gif"};
    private static final long MAX_SIZE = 5L * 1024 * 1024;

    @Override
    public Sensitiveclientdata applyOrUpdate(Integer clientId, String embg, MultipartFile portrait) throws IOException {
        if (embg == null || embg.isBlank()) throw new IllegalArgumentException("EMBG is required");
        if (portrait == null || portrait.isEmpty()) throw new IllegalArgumentException("Portrait photo is required");

        Client client = clientRepo.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));

        validateImage(portrait);
        Path base = ensureUploadPath();
        String name = Optional.ofNullable(portrait.getOriginalFilename()).orElse("portrait");
        String ext = getExt(name);
        String storedName = "portrait_" + clientId + "_" + UUID.randomUUID() + "." + ext;
        Path dest = base.resolve(storedName);
        Files.copy(portrait.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        Optional<Sensitiveclientdata> existingOpt = repo.findFirstByClient_IdOrderByIdDesc(clientId);
        Sensitiveclientdata row = existingOpt.orElseGet(Sensitiveclientdata::new);

        if (existingOpt.isPresent()) {
            deletePhysicalIfExists(existingOpt.get().getPortraitPhoto());
        }

        row.setClient(client);
        row.setPharmacist(null);
        row.setEmbg(embg.trim());
        row.setPortraitPhoto(storedName);
        row.setVerificationStatus("во тек");

        return repo.save(row);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sensitiveclientdata> latestForClient(Integer clientId) {
        return repo.findFirstByClient_IdOrderByIdDesc(clientId);
    }

    /* Helpers */
    private Path ensureUploadPath() throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        return dir;
    }

    private void validateImage(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_SIZE) throw new IOException("File exceeds 5MB");
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) throw new IOException("Invalid filename");
        String ext = getExt(original);
        boolean ok = java.util.Arrays.stream(ALLOWED).anyMatch(a -> a.equalsIgnoreCase(ext));
        if (!ok) throw new IOException("Not an allowed image type");
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) throw new IOException("Not an image");
    }

    private String getExt(String fn) {
        int i = fn.lastIndexOf('.');
        return (i == -1) ? "jpg" : fn.substring(i+1).toLowerCase(Locale.ROOT);
    }

    private void deletePhysicalIfExists(String storedName) throws IOException {
        if (storedName == null || storedName.isBlank()) return;
        Path p = Paths.get(uploadDir).resolve(Paths.get(storedName).getFileName().toString());
        if (Files.exists(p)) Files.delete(p);
    }
}
