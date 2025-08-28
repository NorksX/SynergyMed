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
public class AllergicreactionHealthprofileMedicineId implements Serializable {
    private static final long serialVersionUID = -1373862824826512311L;
    @Column(name = "health_profile_id", nullable = false)
    private Integer healthProfileId;

    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    public AllergicreactionHealthprofileMedicineId() {}

    public AllergicreactionHealthprofileMedicineId(Integer healthProfileId, Integer medicineId) {
        this.healthProfileId = healthProfileId;
        this.medicineId = medicineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AllergicreactionHealthprofileMedicineId entity = (AllergicreactionHealthprofileMedicineId) o;
        return Objects.equals(this.medicineId, entity.medicineId) &&
                Objects.equals(this.healthProfileId, entity.healthProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicineId, healthProfileId);
    }

}