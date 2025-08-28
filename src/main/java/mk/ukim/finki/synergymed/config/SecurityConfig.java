package mk.ukim.finki.synergymed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())   // disable CSRF if not needed
                .formLogin(form -> form.disable()) // disable form login
                .httpBasic(basic -> basic.disable()); // disable basic auth
        return http.build();
    }
}
