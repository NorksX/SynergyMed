package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query("SELECT c FROM Client c WHERE c.id NOT IN (SELECT h.client.id FROM Healthprofile h)")
    List<Client> findClientsWithoutHealthProfile();

    @Query("SELECT c FROM Client c WHERE c.id NOT IN (SELECT h.client.id FROM Healthprofile h) " +
            "AND (LOWER(c.users.firstName) LIKE :searchTerm OR LOWER(c.users.lastName) LIKE :searchTerm)")
    List<Client> findClientsWithoutHealthProfileByName(@Param("searchTerm") String searchTerm);

}