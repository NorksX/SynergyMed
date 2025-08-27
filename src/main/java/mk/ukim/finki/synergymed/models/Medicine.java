package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medicine", schema = "synergymed")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "active_ingredient", nullable = false)
    private String activeIngredient;

}