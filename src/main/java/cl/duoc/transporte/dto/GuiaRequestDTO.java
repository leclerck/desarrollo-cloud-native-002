package cl.duoc.transporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuiaRequestDTO {

    @NotBlank
    private String numeroGuia;

    @NotBlank
    private String transportista;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String destinatario;

    @NotBlank
    private String direccionDestino;

    @NotBlank
    private String descripcionCarga;
}
