package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Clientorder;

import java.util.List;
import java.util.Optional;

public interface ClientOrderService {
    List<Clientorder> findAllForClient(Integer clientId);
    Optional<Clientorder> findByIdForClient(Integer orderId, Integer clientId);
}
