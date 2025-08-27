package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "brandedmedicine", schema = "synergymed")
public class Brandedmedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "dosage_form", nullable = false, length = 100)
    private String dosageForm;

    @Column(name = "strength", nullable = false, length = 100)
    private String strength;

    @Column(name = "origin_country", length = 100)
    private String originCountry;

    @Column(name = "name", nullable = false)
    private String name;

}