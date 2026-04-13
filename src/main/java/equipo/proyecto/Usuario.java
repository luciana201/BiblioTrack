package equipo.proyecto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private List<Reseña> reseñas;
    private HashMap<String, EstadoLectura> estadosLectura;

    public Usuario(String pnombre, String uID, String pemail) {
        this.nombre = pnombre;
        this.email = pemail;
        this.id = uID;
        this.reseñas = new ArrayList<>();
        this.estadosLectura = new HashMap<>();
    }

    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Reseña> getReseñas() {
        return this.reseñas;
    }

    public void setNombre(String pnombre) {
        this.nombre = pnombre;
    }

    public void setEmail(String pemail) {
        this.email = pemail;
    }

    public void agregarReseña(Reseña reseña) {
        this.reseñas.add(reseña);
    }

    public void setEstadoLibro(String idLibro, EstadoLectura estado) {
        estadosLectura.put(idLibro, estado);
    }

    public EstadoLectura getEstadoLibro(String idLibro) {
        return estadosLectura.getOrDefault(
            idLibro, EstadoLectura.PENDIENTE);
    }

    public HashMap<String, EstadoLectura> getEstadosLectura() {
        return estadosLectura;
    }

    public String toJson() {
        return "{" +
                "\"id\":\"" + escapeJson(id) + "\"," +
                "\"nombre\":\"" + escapeJson(nombre) + "\"," +
                "\"email\":\"" + escapeJson(email) + "\"" +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    @Override
    public String toString() {
        return nombre + " <" + email + ">";
    }
}