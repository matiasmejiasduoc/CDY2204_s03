package cl.duoc.gestion_guias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuideRequestDTO {

    @NotBlank
    private String guideNumber;

    @NotBlank
    private String carrier;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String recipient;

    @NotBlank
    private String destinationAddress;

    @NotBlank
    private String cargoDescription;
}
