package mk.ukim.finki.synergymed.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
public class ShoppingcartBrandedmedicineId implements Serializable {
    private static final long serialVersionUID = -1731196071675375973L;
    @Column(name = "shopping_cart_id", nullable = false)
    private Integer shoppingCartId;

    @Column(name = "branded_medicine_id", nullable = false)
    private Integer brandedMedicineId;

    public ShoppingcartBrandedmedicineId(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShoppingcartBrandedmedicineId entity = (ShoppingcartBrandedmedicineId) o;
        return Objects.equals(this.brandedMedicineId, entity.brandedMedicineId) &&
                Objects.equals(this.shoppingCartId, entity.shoppingCartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandedMedicineId, shoppingCartId);
    }

}