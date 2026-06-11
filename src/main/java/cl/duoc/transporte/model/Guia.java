package cl.duoc.transporte.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "guias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroGuia;

    @Column(nullable = false)
    private String transportista;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String destinatario;

    @Column(nullable = false)
    private String direccionDestino;

    @Column(nullable = false)
    private String descripcionCarga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuiaStatus status;

    private String rutaEfs;

    private String s3Key;
}
