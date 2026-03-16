package main.java.equipo.proyecto;
import java.time.LocalDate;

public class Reseña {
    private int id;
    private String comentario;
    private int calificacion; //1 a 10
    private LocalDate fecha;
    private Usuario usuario;
    private Publicacion publicacion;
    public Reseña(String pcomentario, int pcalificacion, Usuario pusuario, Publicacion ppublicacion){
        this.comentario = pcomentario;
        this.calificacion = pcalificacion;
        this.usuario = pusuario;
        this.publicacion = ppublicacion;
        this.fecha = LocalDate.now();
    }
    public int getId(){
        return this.id;
    }
    public String getComentario(){
        return this.comentario;
    }
    public int getCalificacion(){
        return this.calificacion;
    }
    public LocalDate getFecha(){
        return this.fecha;
    }
    public Usuario getUsuario(){
        return this.usuario;
    }
    public Publicacion getPublicacion(){
        return this.publicacion;
    }
    public void setComentario(String pcomentario){
        this.comentario = pcomentario;
    }
    public void setCalificacion(int pcalificacion){
        this.calificacion = pcalificacion;
    }
    public String toString(){
        return "Reseña de " + this.usuario.getNombre() + " para " + this.publicacion.getTitulo() + ": " + this.comentario + " (Calificación: " + this.calificacion + "/10)";
    }
}
