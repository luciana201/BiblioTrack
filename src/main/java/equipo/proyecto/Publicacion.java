package equipo.proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Publicacion {

    private static int siguienteId = 1;
    private final String id;
    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String isbn;
    private String genero;
    private EstadoLectura estado;
    private List<Reseña> reseñas;

    protected Publicacion(String id, String titulo, String autor, int añoPublicacion, String isbn, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.isbn = isbn;
        this.genero = genero;
        this.estado = EstadoLectura.PENDIENTE;
        this.reseñas = new ArrayList<>();
        actualizarSiguienteId(id);
    }

    public Publicacion(String titulo, String autor, int añoPublicacion, String isbn, String genero) {
        this(String.valueOf(siguienteId++), titulo, autor, añoPublicacion, isbn, genero);
    }

    private void actualizarSiguienteId(String id) {
        try {
            int valor = Integer.parseInt(id);
            if (valor >= siguienteId) {
                siguienteId = valor + 1;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public String getId() {
        return id;
    }

    public void agregarReseña(Reseña reseña) {
        reseñas.add(reseña);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenero() {
        return genero;
    }

    public EstadoLectura getEstado() {
        return estado;
    }

    public void setEstado(EstadoLectura estado) {
        this.estado = estado;
    }

    public List<Reseña> getReseñas() {
        return reseñas;
    }

    public String toJson() {
        return "{" +
                "\"id\":\"" + escapeJson(id) + "\"," +
                "\"tipo\":\"" + escapeJson(getClass().getSimpleName()) + "\"," +
                "\"titulo\":\"" + escapeJson(titulo) + "\"," +
                "\"autor\":\"" + escapeJson(autor) + "\"," +
                "\"añoPublicacion\":" + añoPublicacion + "," +
                "\"isbn\":\"" + escapeJson(isbn) + "\"," +
                "\"genero\":\"" + escapeJson(genero) + "\"," +
                "\"estado\":\"" + escapeJson(estado.name()) + "\"," +
                "\"reseñas\":" + reseñasToJson() +
                getJsonExtra() +
                "}";
    }

    protected String getJsonExtra() {
        return "";
    }

    private String reseñasToJson() {
        return "[" + reseñas.stream()
                .map(r -> "{\"usuario\":\"" + escapeJson(r.getUsuario().getNombre()) +
                        "\",\"comentario\":\"" + escapeJson(r.getComentario()) +
                        "\",\"calificacion\":" + r.getCalificacion() +
                        "}")
                .collect(Collectors.joining(",")) + "]";
    }

    protected String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public abstract String getInfoDetallada();

}
