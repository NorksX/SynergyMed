package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "distributor_brandedmedicine", schema = "synergymed")
public class DistributorBrandedmedicine {
    @EmbeddedId
    private DistributorBrandedmedicineId id;

    @MapsId("distributorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "distributor_id", nullable = false)
    private Distributor distributor;

    @MapsId("brandedMedicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

}