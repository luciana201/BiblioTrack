package main.java.equipo.proyecto;
public class Novela extends Publicacion {

    private String tipoNarrador;
    private int numeroPaginas;

    public Novela(String titulo, String autor, int añoPublicacion,
                  String isbn, String genero, String tipoNarrador,
                  int numeroPaginas) {

        super(titulo, autor, añoPublicacion, isbn, genero);
        this.tipoNarrador = tipoNarrador;
        this.numeroPaginas = numeroPaginas;
    }

    @Override
    public String getGenero() {
        return "Novela";
    }

    @Override
    public String getInfoDetallada() {
        return "Narrador: " + tipoNarrador +
               " | Paginas: " + numeroPaginas;
    }

    public String getTipoNarrador() {
        return tipoNarrador;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setTipoNarrador(String tipoNarrador) {
        this.tipoNarrador = tipoNarrador;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
}