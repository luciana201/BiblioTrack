package main.java.equipo.proyecto;

import java.util.List;

public class Usuario{
    private int id;
    private String nombre;
    private String email;
    private List<Reseña> reseñas;

    public Usuario (String pnombre, String pemail){
        this.nombre = pnombre;
        this.email = pemail;
    }

    public int getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getEmail(){
        return this.email;
    }

    public List<Reseña> getReseñas(){
        return this.reseñas;
    }

    public void setNombre(String pnombre){
        this.nombre = pnombre;
    }

    public void setEmail(String pemail){
        this.email = pemail;
    }

    public void agregarReseña(Reseña reseñas){

    }

    public String toString(String cadena){
        return cadena;
    }

}

