package equipo.proyecto;

public class LibroTecnico extends Publicacion {
    
    private String tema;
    private String nivel;

    public LibroTecnico(String titulo, String autor, int añoPublicacion, 
                        String isbn, String genero, String tema, String nivel) {
        super(titulo, autor, añoPublicacion, isbn, genero);
        this.tema = tema;
        this.nivel = nivel;
    }


    @Override
    public String getGenero() {
        return "Libro Técnico";
    }

    @Override
    public String getInfoDetallada() {
        return "Tema: " + tema + " | Nivel: " + nivel;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}