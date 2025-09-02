package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.Brandedmedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandedmedicineRepository extends JpaRepository<Brandedmedicine, Integer> {
    @Query(value = """
        select distinct bm.*
        from synergymed.brandedmedicine bm
        join synergymed.inventory_brandedmedicine ibm on ibm.branded_medicine_id = bm.id
        join synergymed.inventory i on i.id = ibm.inventory_id
        join synergymed.facility f on f.id = i.facility_id
        join synergymed.company c on c.id = f.company_id
        join synergymed.pharmacy p on p.company_id = c.id
        where p.id = :pharmacyId
        """, nativeQuery = true)
    List<Brandedmedicine> findAllAvailableForPharmacy(@Param("pharmacyId") Integer pharmacyId);
}