package mk.ukim.finki.synergymed.service;

import mk.ukim.finki.synergymed.models.Distributor;

import java.util.List;
import java.util.Optional;

public interface DistributorService {
    List<Distributor> findAll();
    Optional<Distributor> findById(Integer companyId);
    Distributor create(Integer companyId);
    void deleteById(Integer companyId);
}
