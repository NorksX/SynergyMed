package mk.ukim.finki.synergymed.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MedicineinteractionId implements Serializable {
    private static final long serialVersionUID = 3958999586526240772L;
    @Column(name = "medicine_id_1", nullable = false)
    private Integer medicineId1;

    @Column(name = "medicine_id_2", nullable = false)
    private Integer medicineId2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MedicineinteractionId entity = (MedicineinteractionId) o;
        return Objects.equals(this.medicineId2, entity.medicineId2) &&
                Objects.equals(this.medicineId1, entity.medicineId1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicineId2, medicineId1);
    }

}