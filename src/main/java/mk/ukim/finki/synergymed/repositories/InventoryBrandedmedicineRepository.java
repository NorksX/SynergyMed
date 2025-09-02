package mk.ukim.finki.synergymed.repositories;

import jakarta.persistence.LockModeType;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicine;
import mk.ukim.finki.synergymed.models.InventoryBrandedmedicineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface InventoryBrandedmedicineRepository extends JpaRepository<InventoryBrandedmedicine, InventoryBrandedmedicineId> {

    @Query("""
       select ibm
       from InventoryBrandedmedicine ibm
         join fetch ibm.brandedMedicine bm
         join fetch bm.manufacturer m
         join fetch m.company c
       where ibm.inventory.id = :inventoryId
       order by ibm.lastChanged desc
       """)
    List<InventoryBrandedmedicine> findAllWithMedicineByInventoryId(@Param("inventoryId") Integer inventoryId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           select ibm
           from InventoryBrandedmedicine ibm
           where ibm.brandedMedicine.id = :bmId
           order by ibm.quantity desc
           """)
    List<InventoryBrandedmedicine> lockAllByMedicineOrderByQuantityDesc(@Param("bmId") Integer bmId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select ibm
       from InventoryBrandedmedicine ibm
         join ibm.inventory inv
         join inv.facility fac
       where ibm.brandedMedicine.id = :bmId
         and exists (
             select 1 from Pharmacy p
             where p.id = fac.company.id
         )
       order by ibm.quantity desc
       """)
    List<InventoryBrandedmedicine> lockAllByMedicineInPharmacies(@Param("bmId") Integer bmId);

        @Query("""
      select ibm.brandedMedicine.id, coalesce(sum(ibm.quantity), 0)
      from InventoryBrandedmedicine ibm
      where ibm.brandedMedicine.id in :ids
      group by ibm.brandedMedicine.id
    """)
        List<Object[]> sumByBrandedMedicineIds(@Param("ids") Collection<Integer> ids);

    @Query("""
      select new map(ibm.brandedMedicine.id as id, sum(ibm.quantity) as qty)
      from InventoryBrandedmedicine ibm
      join ibm.inventory inv
      join inv.facility f
      join f.company c
      where ibm.brandedMedicine.id in :ids
        and exists (
          select 1 from Pharmacy ph
          where ph.id = c.id
        )
      group by ibm.brandedMedicine.id
""")
    List<Map<String,Object>> sumAsMapForPharmacies(@Param("ids") Collection<Integer> ids);



    }


