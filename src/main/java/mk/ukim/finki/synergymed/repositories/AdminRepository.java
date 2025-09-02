package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    boolean existsById(Integer userId);

}