package cl.duoc.gestion_guias_worker.dto;

import cl.duoc.gestion_guias_worker.model.GuideStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuideMessageDTO {

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
