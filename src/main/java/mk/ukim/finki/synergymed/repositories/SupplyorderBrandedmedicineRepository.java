package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicine;
import mk.ukim.finki.synergymed.models.SupplyorderBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplyorderBrandedmedicineRepository extends JpaRepository<SupplyorderBrandedmedicine, SupplyorderBrandedmedicineId> {
    @Query("""
       select sobm
       from SupplyorderBrandedmedicine sobm
         join fetch sobm.brandedMedicine bm
       where sobm.supplyOrder.id = :orderId
       order by bm.name asc
    """)
    List<SupplyorderBrandedmedicine> findAllBySupplyOrderIdFetchMedicine(@Param("orderId") Integer orderId);
}