package equipo.proyecto;

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
        this.publicaciones.add(publicacion); // lo agrega y ya
    }  

    public boolean eliminarPublicacion(String titulo){
        for (int i = 0; i < publicaciones.size(); i++) {
            if (publicaciones.get(i).getTitulo().equalsIgnoreCase(titulo)) { //verifica que exista la publicacion
                publicaciones.remove(i);
                return true;
            }
        }
        return false; //si no existe no hay nada que borrar
    }
    public Publicacion buscarPublicacion(String titulo){
        for (Publicacion p : publicaciones) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) { //verifica q exista
                return p; //lo retorna
            }
        }
        return null;
    }
    public ArrayList<Publicacion> filtrarPorGenero(String genero){
        ArrayList<Publicacion> resultado = new ArrayList<>();
        for (Publicacion p : publicaciones) {
            if (p.getGenero() != null && p.getGenero().equalsIgnoreCase(genero)) { //cheka q exista
                resultado.add(p);
            }
        }
        return resultado;
    }
    public ArrayList<Publicacion> filtrarPorEstado(EstadoLectura estado){
        ArrayList<Publicacion> resultado = new ArrayList<>();
        for (Publicacion p : publicaciones) {
            if (p.getEstado() == estado) {
                resultado.add(p); //devulde solo si cumple con el estado de lectura
            }
        }
        return resultado;
    }
    public ArrayList<Publicacion> filtrarPorCalificacion(int calificacionMinima){
        ArrayList<Publicacion> resultado = new ArrayList<>();
        for (Publicacion p : publicaciones) {
            int suma = 0;
            int cant = 0;
            for (Reseña r : p.getReseñas()) {
                suma += r.getCalificacion();
                cant++;
            }
            if (cant > 0) {
                int promedio = suma / cant;
                if (promedio >= calificacionMinima) {
                    resultado.add(p); //si cumple con la calificacion minima
                                    // se agrega a la lista de resultados
                }
            }
        }
        return resultado;
    }
    public void agregarUsuario(Usuario usuario){
        if (usuario != null) {
            this.usuarios.add(usuario);
        }
    }
    public boolean eliminarUsuario(String nombre){
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombre().equalsIgnoreCase(nombre)) {//verifica que exista
                usuarios.remove(i);//elimina el usuario
                return true;
            }
        }
        return false; //si no existe no hay nd q borrar
    }
    public Usuario buscarUsuarioID(String nombre){
        for (Usuario u : usuarios) { //Recorre la lista de usuarios
            if (u.getNombre().equalsIgnoreCase(nombre) || u.getId().equalsIgnoreCase(nombre)) {
                return u;
            }
        }
        return null; // si no hay nada retorna null
    }
    public boolean agregarReseña(String tituloPublicacion, Reseña reseña){
        Publicacion p = buscarPublicacion(tituloPublicacion);
        if (p != null && reseña != null) { //tiene q cumplir -> q exista publicacion y reseña ingresada
            p.agregarReseña(reseña);       //para recien agregar la reseña
            return true;
        }
        return false;
    }
    public Publicacion getLibroMejorCalificado(){
        Publicacion mejor = null;
        double mejorProm = -1;
        for (Publicacion p : publicaciones) {
            int suma = 0;
            int cant = 0;
            for (Reseña r : p.getReseñas()) {
                suma += r.getCalificacion();
                cant++;
            }
            if (cant > 0) { //solo cuenta si esq la publi tiene reseñas
                double promedio = (double) suma / cant;
                if (promedio > mejorProm) {
                    mejorProm = promedio;
                    mejor = p;
                }
            }
        }
        return mejor;
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
