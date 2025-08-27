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
public class ClientorderBrandedmedicineId implements Serializable {
    private static final long serialVersionUID = 4967247143893490519L;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClientorderBrandedmedicineId entity = (ClientorderBrandedmedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.orderId, entity.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, orderId);
    }

}