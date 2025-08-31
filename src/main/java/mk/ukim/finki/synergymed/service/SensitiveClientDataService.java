package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface SensitiveClientDataService {
    Sensitiveclientdata applyOrUpdate(Integer clientId, String embg, MultipartFile portrait) throws IOException;

    Optional<Sensitiveclientdata> latestForClient(Integer clientId);
}
