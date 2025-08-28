package mk.ukim.finki.synergymed.config;

import org.springframework.stereotype.Component;

@Component
class ConfigProbe {
    ConfigProbe(org.springframework.core.env.Environment env) {
        String present = env.containsProperty("security.jwt.secret") ? "YES" : "NO";
        System.out.println(">> security.jwt.secret present? " + present);
    }
}

