package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    List<Facility> findAllByCompanyId(Integer companyId);
    boolean existsByCompanyId(Integer companyId);
}