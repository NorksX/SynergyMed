// mk/ukim/finki/synergymed/service/impl/ClientOrderServiceImpl.java
package mk.ukim.finki.synergymed.service.impl;

import mk.ukim.finki.synergymed.models.Clientorder;
import mk.ukim.finki.synergymed.repositories.ClientorderRepository;
import mk.ukim.finki.synergymed.service.ClientOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientorderRepository repo;

    public ClientOrderServiceImpl(ClientorderRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Clientorder> findAllForClient(Integer clientId) {
        return repo.findAllByClientIdOrderByOrderDateDesc(clientId);
    }

    @Override
    public Optional<Clientorder> findByIdForClient(Integer orderId, Integer clientId) {
        return repo.findDetailForClient(orderId, clientId);
    }
}
