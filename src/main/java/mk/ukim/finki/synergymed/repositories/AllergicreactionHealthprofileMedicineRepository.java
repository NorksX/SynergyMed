package mk.ukim.finki.synergymed.repositories;

import mk.ukim.finki.synergymed.models.AllergicreactionHealthprofileMedicine;
import mk.ukim.finki.synergymed.models.AllergicreactionHealthprofileMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergicreactionHealthprofileMedicineRepository extends JpaRepository<AllergicreactionHealthprofileMedicine, AllergicreactionHealthprofileMedicineId> {
}