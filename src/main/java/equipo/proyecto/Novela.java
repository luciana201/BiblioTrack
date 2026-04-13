package equipo.proyecto;

public class Novela extends Publicacion {

    private int numeroPaginas;

    public Novela(String titulo, String tipo, String autor, int añoPublicacion, String isbn, String genero,
                  int numeroPaginas) { 
        super(titulo, tipo, autor, añoPublicacion, isbn, genero);
        this.numeroPaginas = numeroPaginas;
    }

    public Novela(String id, String titulo, String tipo, String autor, int añoPublicacion,
                  String isbn, String genero, int numeroPaginas) {
        super(id, titulo, tipo, autor, añoPublicacion, isbn, genero);
        this.numeroPaginas = numeroPaginas;
    }

    @Override
    public String getInfoDetallada() {
        return "Paginas: " + numeroPaginas;
    }

    @Override
    public String getTipo() {
        return "Novela";
    }

    @Override
    protected String getJsonExtra() {
        return ",\"numeroPaginas\":" + numeroPaginas;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }
}