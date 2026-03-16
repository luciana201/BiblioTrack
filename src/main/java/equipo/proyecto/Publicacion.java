package main.java.equipo.proyecto;
import java.util.ArrayList;
import java.util.List;

public class Publicacion {
}
 */
public abstract class Publicacion

    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String isbn;
    private String genero;
    private EstadoLectura estado;
    private List<Resena> resenas;
    private String[] etiquetas; 

    /**

     * @param titulo 
     * @param autor 
     * @param añoPublicacion 
     * @param isbn 
     * @param genero 
     * @throws IllegalArgumentException
     */
    public Publicacion(String titulo, String autor, int añoPublicacion, 
                       String isbn, String genero) {
        
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        if (añoPublicacion <= 0) {
            throw new IllegalArgumentException("El año debe ser positivo");
        }
        if (isbn == null || !validarISBN(isbn)) {
            throw new IllegalArgumentException("ISBN no válido");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío");
        }

        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.isbn = isbn;
        this.genero = genero;
        this.estado = EstadoLectura.PENDIENTE; 
        this.resenas = new ArrayList<>();
        this.etiquetas = new String[5]; 
    }

    /**
     
     * @param isbn
     * @return 
     */
    private boolean validarISBN(String isbn) {
       
        String regexISBN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        return isbn.matches(regexISBN);
    }

    
    
    /**
     * 
     * @return 
     */
    public abstract String getTipo();
    
    /**
     * 
     * @return 
     */
    public abstract String getInfoDetallada();

    
    
    /**
     * 
     * @param resena 
     */
    public void agregarResena(Resena resena) {
        if (resena != null) {
            this.resenas.add(resena);
        }
    }

    /**
     * 
     * @return 
     */
    public double getValoracionMedia() {
        if (resenas.isEmpty()) return 0.0;
        return resenas.stream()
                .mapToInt(Resena::getPuntuacion)
                .average()
                .orElse(0.0);
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        this.autor = autor;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(int añoPublicacion) {
        if (añoPublicacion <= 0) {
            throw new IllegalArgumentException("El año debe ser positivo");
        }
        this.añoPublicacion = añoPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || !validarISBN(isbn)) {
            throw new IllegalArgumentException("ISBN no válido");
        }
        this.isbn = isbn;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío");
        }
        this.genero = genero;
    }

    public EstadoLectura getEstado() {
        return estado;
    }

    public void setEstado(EstadoLectura estado) {
        this.estado = estado;
    }

    public List<Resena> getResenas() {
        return new ArrayList<>(resenas); 
    }

    public String[] getEtiquetas() {
        return etiquetas.clone(); 
    }

    public void setEtiquetas(String[] etiquetas) {
        if (etiquetas != null) {
            this.etiquetas = etiquetas.clone();
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d) [%s] - %s", 
            titulo, autor, añoPublicacion, getTipo(), estado);
    }
}