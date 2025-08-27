package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "prescription", schema = "synergymed")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "issued_by", nullable = false, length = 50)
    private String issuedBy;

    @Column(name = "issued_at", nullable = false)
    private LocalDate issuedAt;

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @Column(name = "embg", nullable = false, length = 20)
    private String embg;

}