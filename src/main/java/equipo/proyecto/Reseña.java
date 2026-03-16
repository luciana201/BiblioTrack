package main.java.equipo.proyecto;
import java.time.LocalDate;

public class Reseña {
    private int id;
    private String comentario;
    private int calificacion; //1 a 10
    private LocalDate fecha;
    private Usuario usuario;
    private Publicacion publicacion;
}
