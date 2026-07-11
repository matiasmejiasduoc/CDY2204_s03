package cl.duoc.gestion_guias.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_guides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedGuide {

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

    @Column(nullable = false)
    private LocalDateTime processedAt;
}
