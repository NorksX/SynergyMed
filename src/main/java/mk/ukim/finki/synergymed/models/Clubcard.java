package mk.ukim.finki.synergymed.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clubcard", schema = "synergymed")
public class Clubcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Client user;

    @Column(name = "club_program", nullable = false, length = 100)
    private String clubProgram;

    @Column(name = "points", nullable = false)
    private Integer points;

}