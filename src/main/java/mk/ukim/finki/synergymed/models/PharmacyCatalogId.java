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
public class PharmacyCatalogId implements Serializable {
    private static final long serialVersionUID = 1717201987474340712L;
    @Column(name = "pharmacy_id", nullable = false)
    private Integer pharmacyId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PharmacyCatalogId entity = (PharmacyCatalogId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.pharmacyId, entity.pharmacyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, pharmacyId);
    }

}