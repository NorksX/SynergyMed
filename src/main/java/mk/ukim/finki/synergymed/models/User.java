package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "synergymed")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "e_mail", nullable = false)
    private String email;

    @Column(name = "gender", length = 50)
    private String gender;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }

    @Override public String getUsername() { return username; }
    @Override
    public String getPassword() {
        return hashedPassword;
    }
}