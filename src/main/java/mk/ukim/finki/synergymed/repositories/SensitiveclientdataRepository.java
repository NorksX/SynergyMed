package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Sensitiveclientdata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensitiveclientdataRepository extends JpaRepository<Sensitiveclientdata, Integer> {
    Optional<Sensitiveclientdata> findFirstByClient_IdOrderByIdDesc(Integer clientId);
    boolean existsByClient_Id(Integer clientId);
    List<Sensitiveclientdata> findByVerificationStatusOrderByIdAsc(String verificationStatus);
    Optional<Sensitiveclientdata> findById(Integer id);
}
