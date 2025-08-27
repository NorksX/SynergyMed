package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}