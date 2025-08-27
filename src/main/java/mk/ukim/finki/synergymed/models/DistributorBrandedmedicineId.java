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
public class DistributorBrandedmedicineId implements Serializable {
    private static final long serialVersionUID = -1828218854636307454L;
    @Column(name = "distributor_id", nullable = false)
    private Integer distributorId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DistributorBrandedmedicineId entity = (DistributorBrandedmedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.distributorId, entity.distributorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, distributorId);
    }

}