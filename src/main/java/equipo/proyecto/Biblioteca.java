package main.java.equipo.proyecto;

import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {
    private String nombre;
    private ArrayList<Publicacion> publicaciones;
    private HashMap<String, ArrayList<Publicacion>> categorias;
    private ArrayList<Usuario> usuarios;
    public Biblioteca(){
        this.publicaciones = new ArrayList<>();
        this.categorias = new HashMap<>();
        this.usuarios = new ArrayList<>();
    }

    public void agregarPublicacion(Publicacion publicacion){
        this.publicaciones.add(publicacion);
    }  
    public boolean eliminarPublicacion(String titulo){
        return false;
    }
    public Publicacion buscarPublicacion(String titulo){
        return null;
    }
    public ArrayList<Publicacion> filtrarPorGenero(String genero){
        return null;
    }
    public ArrayList<Publicacion> filtrarPorEstado(EstadoLectura estado){
        return null;
    }
    public ArrayList<Publicacion> filtrarPorCalificacion(int calificacionMinima){
        return null;
    }
    public void agregarUsuario(Usuario usuario){
    }
    public boolean eliminarUsuario(String nombre){
        return false;
    }
    public Usuario buscarUsuarioID(String nombre){
        return null;
    }
    public boolean agregarReseña(String tituloPublicacion, Reseña reseña){
        return false;
    }
    public Publicacion getLibroMejorCalificado(){
        return null;
    }
    public Publicacion getAutoresMasLeidos(){
        return null;
    }
    public ArrayList<Publicacion> getLibrosEstado(EstadoLectura estado){
        return publicaciones;
    }
    public ArrayList<Publicacion> getLibrosGenero(String genero){
        return publicaciones;
    }
    public ArrayList<Publicacion> getLibros(){
        return publicaciones;
    }
}
