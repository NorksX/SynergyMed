package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medicineinteraction", schema = "synergymed")
public class Medicineinteraction {
    @EmbeddedId
    private MedicineinteractionId id;

    @MapsId("medicineId1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id_1", nullable = false)
    private Medicine medicineId1;

    @MapsId("medicineId2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id_2", nullable = false)
    private Medicine medicineId2;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "severity", nullable = false, length = 50)
    private String severity;

}