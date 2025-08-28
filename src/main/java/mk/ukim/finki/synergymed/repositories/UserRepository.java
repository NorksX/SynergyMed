package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEMail(String email);
    boolean existsByUsername(String username);
}