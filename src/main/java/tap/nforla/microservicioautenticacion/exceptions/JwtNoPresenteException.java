package tap.nforla.microservicioautenticacion.exceptions;

public class JwtNoPresenteException extends Exception {

    public JwtNoPresenteException(String message) {
        super(message);
    }
}
