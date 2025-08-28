package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "allergicreaction_healthprofile_medicine", schema = "synergymed")
public class AllergicreactionHealthprofileMedicine {
    @EmbeddedId
    private AllergicreactionHealthprofileMedicineId id;

    @MapsId("healthProfileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "health_profile_id", nullable = false)
    private Healthprofile healthProfile;

    @MapsId("medicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "date_diagnosed")
    private LocalDate dateDiagnosed;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "severity", length = 50)
    private String severity;

}