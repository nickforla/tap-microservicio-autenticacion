package tap.nforla.microservicioautenticacion.model;

public enum Rol {
    USER("USER"), ADMIN("ADMIN");

    private String rol;

    Rol(String rol) {
        this.rol = rol;
    }
}
