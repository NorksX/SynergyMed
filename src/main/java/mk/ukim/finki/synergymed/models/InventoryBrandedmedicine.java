package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "inventory_brandedmedicine", schema = "synergymed")
public class InventoryBrandedmedicine {
    @EmbeddedId
    private InventoryBrandedmedicineId id;

    @MapsId("inventoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_changed", nullable = false)
    private LocalDate lastChanged;

}