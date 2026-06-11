package cl.duoc.transporte.dto;

import cl.duoc.transporte.model.GuiaStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuiaResponseDTO {

    private Long id;
    private String numeroGuia;
    private String transportista;
    private LocalDate fecha;
    private String destinatario;
    private String direccionDestino;
    private String descripcionCarga;
    private GuiaStatus status;
    private String s3Key;
}
