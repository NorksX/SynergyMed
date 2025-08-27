package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}