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
public class InventoryBrandedmedicineId implements Serializable {
    private static final long serialVersionUID = 7027365878508193709L;
    @Column(name = "inventory_id", nullable = false)
    private Integer inventoryId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventoryBrandedmedicineId entity = (InventoryBrandedmedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.inventoryId, entity.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, inventoryId);
    }

}