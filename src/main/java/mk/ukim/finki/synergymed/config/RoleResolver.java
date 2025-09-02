package mk.ukim.finki.synergymed.config;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.repositories.AdminRepository;
import mk.ukim.finki.synergymed.repositories.PharmacistRepository;
import mk.ukim.finki.synergymed.repositories.ClientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleResolver {

    private final AdminRepository adminRepo;
    private final PharmacistRepository pharmacistRepo;
    private final ClientRepository clientRepo;

    // Returns raw roles: e.g., ["ADMIN","PHARMACIST","CLIENT"]
    public List<String> rolesForUser(Integer userId) {
        List<String> roles = new ArrayList<>();
        if (adminRepo.existsById(userId)) roles.add("ADMIN");
        if (pharmacistRepo.existsById(userId)) roles.add("PHARMACIST");
        if (clientRepo.existsById(userId)) roles.add("CLIENT");
        return roles;
    }

    public List<SimpleGrantedAuthority> authoritiesForUser(Integer userId) {
        List<String> roles = rolesForUser(userId);
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
    }
}
