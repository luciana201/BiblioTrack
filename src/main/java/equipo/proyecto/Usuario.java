package equipo.proyecto;

import java.util.List;

public class Usuario{
    private String id;
    private String nombre;
    private String email;
    private List<Reseña> reseñas;

    public Usuario (String pnombre, String uID, String pemail){ // uID = ID unico de usuario
        this.nombre = pnombre;
        this.email = pemail;
        this.id = uID;
    }

    public String getId(){
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

