package cl.duoc.transporte.repository;

import cl.duoc.transporte.model.Guia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, Long> {

    List<Guia> findByTransportistaAndFecha(String transportista, LocalDate fecha);

    List<Guia> findByTransportista(String transportista);
}
