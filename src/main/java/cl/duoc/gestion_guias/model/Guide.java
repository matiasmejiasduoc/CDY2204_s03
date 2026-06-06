package cl.duoc.gestion_guias.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "guides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String guideNumber;

    @Column(nullable = false)
    private String carrier;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String destinationAddress;

    @Column(nullable = false)
    private String cargoDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuideStatus status;

    private String efsPath;

    private String s3Key;
}
