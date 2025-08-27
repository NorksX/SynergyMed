package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "supplyorder_brandedmedicine", schema = "synergymed")
public class SupplyorderBrandedmedicine {
    @EmbeddedId
    private SupplyorderBrandedmedicineId id;

    @MapsId("supplyOrderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supply_order_id", nullable = false)
    private Supplyorder supplyOrder;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}