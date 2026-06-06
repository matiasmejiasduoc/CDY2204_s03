package cl.duoc.gestion_guias.exception;

public class GuideNotFoundException extends RuntimeException {

    public GuideNotFoundException(Long id) {
        super("Guide not found with id: " + id);
    }
}
