package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Brandedmedicine;
import mk.ukim.finki.synergymed.models.DistributorBrandedmedicine;
import mk.ukim.finki.synergymed.models.DistributorBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface DistributorBrandedmedicineRepository extends JpaRepository<DistributorBrandedmedicine, DistributorBrandedmedicineId> {
    @Query("""
      select dbm.brandedMedicine
      from DistributorBrandedmedicine dbm
      where dbm.id.distributorId = :distributorId
    """)
    List<Brandedmedicine> findMedicinesByDistributor(@Param("distributorId") Integer distributorId);

    @Query("""
      select dbm.id.brandedMedicineId
      from DistributorBrandedmedicine dbm
      where dbm.id.distributorId = :distributorId
        and dbm.id.brandedMedicineId in :bmIds
    """)
    List<Integer> existingForDistributor(@Param("distributorId") Integer distributorId,
                                         @Param("bmIds") Collection<Integer> bmIds);
}