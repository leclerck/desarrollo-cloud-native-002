package cl.duoc.transporte.exception;

public class GuiaNotFoundException extends RuntimeException {

    public GuiaNotFoundException(Long id) {
        super("No se encontró la guía con el id: " + id);
    }
}
