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
public class SupplyorderBrandedmedicineId implements Serializable {
    private static final long serialVersionUID = 7747697239378701103L;
    @Column(name = "supply_order_id", nullable = false)
    private Integer supplyOrderId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SupplyorderBrandedmedicineId entity = (SupplyorderBrandedmedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.supplyOrderId, entity.supplyOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, supplyOrderId);
    }

}