package equipo.proyecto;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private List<Reseña> reseñas;

    public Usuario(String pnombre, String uID, String pemail) {
        this.nombre = pnombre;
        this.email = pemail;
        this.id = uID;
        this.reseñas = new ArrayList<>();
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

    @Override
    public String toString() {
        return nombre + " <" + email + ">";
    }
}
