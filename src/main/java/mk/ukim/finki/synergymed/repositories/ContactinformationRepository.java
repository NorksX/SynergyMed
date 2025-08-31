package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Contactinformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactinformationRepository extends JpaRepository<Contactinformation, Integer> {
    List<Contactinformation> findByUser_Id(Integer userId);
    List<Contactinformation> findByFacility_Id(Integer facilityId);
    Optional<Contactinformation> findByIdAndUser_Id(Integer id, Integer userId);
    Optional<Contactinformation> findByIdAndFacility_Id(Integer id, Integer facilityId);
}