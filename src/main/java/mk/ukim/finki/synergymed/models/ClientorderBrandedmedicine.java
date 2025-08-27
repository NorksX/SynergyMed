package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clientorder_brandedmedicine", schema = "synergymed")
public class ClientorderBrandedmedicine {
    @EmbeddedId
    private ClientorderBrandedmedicineId id;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Clientorder order;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}