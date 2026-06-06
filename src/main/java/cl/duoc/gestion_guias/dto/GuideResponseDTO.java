package cl.duoc.gestion_guias.dto;

import cl.duoc.gestion_guias.model.GuideStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuideResponseDTO {

    private Long id;
    private String guideNumber;
    private String carrier;
    private LocalDate date;
    private String recipient;
    private String destinationAddress;
    private String cargoDescription;
    private GuideStatus status;
    private String s3Key;
}
