package mk.ukim.finki.synergymed.security;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.synergymed.models.User;
import mk.ukim.finki.synergymed.repositories.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetails implements UserDetailsService {
    private final UserRepository users;
    private final AdminRepository admins;
    private final PharmacistRepository pharmacists;
    private final ClientRepository clients;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        if (!u.isEnabled()) throw new RuntimeException("Verify/enable user first.");

        var auths = new ArrayList<GrantedAuthority>();
        if (admins.existsById(u.getId()))      auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (pharmacists.existsById(u.getId())) auths.add(new SimpleGrantedAuthority("ROLE_PHARMACIST"));
        if (clients.existsById(u.getId()))     auths.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        if (auths.isEmpty())                   auths.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), u.isEnabled(),
                u.isAccountNonExpired(), u.isCredentialsNonExpired(), u.isAccountNonLocked(), auths
        );
    }
}
