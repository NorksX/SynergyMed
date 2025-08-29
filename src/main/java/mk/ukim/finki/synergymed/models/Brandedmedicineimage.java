package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "brandedmedicineimage", schema = "synergymed")
public class Brandedmedicineimage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branded_medicine_id", nullable = false)
    private Brandedmedicine brandedMedicine;

    @Column(name = "image", nullable = false)
    private String image;

    // New flag for “main” image
    @Column(name = "is_main_image", nullable = false)
    private boolean mainImage = false;
}
