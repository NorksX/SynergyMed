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
public class BrandedMedicineInstanceofMedicineId implements Serializable {
    private static final long serialVersionUID = -5807151723120425766L;
    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BrandedMedicineInstanceofMedicineId entity = (BrandedMedicineInstanceofMedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.medicineId, entity.medicineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, medicineId);
    }

}