package equipo.proyecto;

public class Comic extends Publicacion {

    private int volumen;
    private String demografia;

    public Comic(String titulo, String autor, int añoPublicacion,
                 String isbn, String genero,int volumen, String demografia) {

        super(titulo, autor, añoPublicacion, isbn, genero);
        this.volumen = volumen;
        this.demografia = demografia;
    }

    public Comic(String id, String titulo, String autor, int añoPublicacion,
                 String isbn, String genero,
                 int volumen, String demografia) {

        super(id, titulo, autor, añoPublicacion, isbn, genero);
        this.volumen = volumen;
        this.demografia = demografia;
    }

    @Override
    public String getGenero() {
        return "Comic";
    }

    @Override
    public String getInfoDetallada() {
        return "Volumen: " + volumen +
               " | Demografia: " + demografia;
    }

    @Override
    protected String getJsonExtra() {
        return "\",\"volumen\":" + volumen +
               ",\"demografia\":\"" + escapeJson(demografia) + "\"";
    }


    public int getVolumen() {
        return volumen;
    }

    public String getDemografia() {
        return demografia;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public void setDemografia(String demografia) {
        this.demografia = demografia;
    }
}