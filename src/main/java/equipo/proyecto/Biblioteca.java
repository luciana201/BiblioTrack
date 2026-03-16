package main.java.equipo.proyecto;

import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {
    private String nombre;
    private ArrayList<Publicacion> publicaciones;
    private HashMap<String, ArrayList<Publicacion>> categorias;
    private ArrayList<Usuario> usuarios;
    public Biblioteca(String pnombre){
        this.nombre = pnombre;
        this.publicaciones = new ArrayList<>();
        this.categorias = new HashMap<>();
        this.usuarios = new ArrayList<>();
    }
}
