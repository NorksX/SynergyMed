package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pharmacy_catalog", schema = "synergymed")
public class PharmacyCatalog {
    @EmbeddedId
    private PharmacyCatalogId id;

    @MapsId("pharmacyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

}