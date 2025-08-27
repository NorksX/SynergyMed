package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sensitiveclientdata", schema = "synergymed")
public class Sensitiveclientdata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacist_id")
    private Pharmacist pharmacist;

    @Column(name = "embg", nullable = false, length = 20)
    private String embg;

    @Column(name = "portrait_photo", nullable = false)
    private String portraitPhoto;

    @Column(name = "verification_status", nullable = false, length = 50)
    private String verificationStatus;

}