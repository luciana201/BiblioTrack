package equipo.proyecto;

public class LibroTecnico extends Publicacion {
    
    private String tema;
    private String nivel;

    public LibroTecnico(String titulo, String tipo, String autor, int añoPublicacion, 
                        String isbn, String genero, String tema, String nivel) {
        super(titulo, tipo, autor, añoPublicacion, isbn, genero);
        this.tema = tema;
        this.nivel = nivel;
    }

    public LibroTecnico(String id,  String titulo, String tipo, String autor, int añoPublicacion, 
                        String isbn, String genero, String tema, String nivel) {
        super(id, titulo, tipo, autor, añoPublicacion, isbn, genero);
        this.tema = tema;
        this.nivel = nivel;
    }

    @Override
    public String getTipo() {
        return "Libro Técnico";
    }

    @Override
    public String getInfoDetallada() {
        return "Tema: " + tema + " | Nivel: " + nivel;
    }

    @Override
    protected String getJsonExtra() {
        return ",\"tema\":\"" + escapeJson(tema) +
               "\",\"nivel\":\"" + escapeJson(nivel) + "\"";
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