package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Clientorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientorderRepository extends JpaRepository<Clientorder, Integer> {

    List<Clientorder> findAllByClientIdOrderByOrderDateDesc(Integer clientId);

    @Query("""
           select o
           from Clientorder o
             join fetch o.deliveryCompany dc
             join fetch o.payment p
           where o.id = :orderId and o.client.id = :clientId
           """)
    Optional<Clientorder> findDetailForClient(Integer orderId, Integer clientId);
}