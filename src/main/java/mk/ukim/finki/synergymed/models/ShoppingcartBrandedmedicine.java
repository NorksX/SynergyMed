package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shoppingcart_brandedmedicine", schema = "synergymed")
public class ShoppingcartBrandedmedicine {
    @EmbeddedId
    private ShoppingcartBrandedmedicineId id;

    @MapsId("shoppingCartId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private Shoppingcart shoppingCart;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}