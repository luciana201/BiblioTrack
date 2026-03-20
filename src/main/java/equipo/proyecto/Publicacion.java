package main.java.equipo.proyecto;
import java.util.List;
import java.util.ArrayList;

public abstract class Publicacion {

    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String isbn;
    private String genero;
    private EstadoLectura estado;
    private List<Reseña> reseñas;

    public Publicacion(String titulo, String autor, int añoPublicacion, String isbn, String genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.isbn = isbn;
        this.genero = genero;
        this.estado = EstadoLectura.PENDIENTE;
        this.reseñas = new ArrayList<>();
    }

    public void agregarReseña(Reseña reseña) {
        reseñas.add(reseña);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenero() {
        return genero;
    }

    public EstadoLectura getEstado() {
        return estado;
    }

    public void setEstado(EstadoLectura estado) {
        this.estado = estado;
    }

    public List<Reseña> getReseñas() {
        return reseñas;
    }
    
    public abstract String getInfoDetallada();

}