package equipo.proyecto;

public class Comic extends Publicacion {

    private String ilustrador;
    private int volumen;
    private String demografia;

    public Comic(String titulo, String autor, int añoPublicacion,
                 String isbn, String genero, String ilustrador,
                 int volumen, String demografia) {

        super(titulo, autor, añoPublicacion, isbn, genero);
        this.ilustrador = ilustrador;
        this.volumen = volumen;
        this.demografia = demografia;
    }

    @Override
    public String getGenero() {
        return "Comic";
    }

    @Override
    public String getInfoDetallada() {
        return "Ilustrador: " + ilustrador +
               " | Volumen: " + volumen +
               " | Demografia: " + demografia;
    }


    public String getIlustrador() {
        return ilustrador;
    }

    public int getVolumen() {
        return volumen;
    }

    public String getDemografia() {
        return demografia;
    }


    public void setIlustrador(String ilustrador) {
        this.ilustrador = ilustrador;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public void setDemografia(String demografia) {
        this.demografia = demografia;
    }
}