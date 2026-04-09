package equipo.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests unitarios para Comic")
public class ComicTest {
    private Comic comic;

    @BeforeEach
    void setUp() {
        comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Comic", 1, "Superhéroes");
    }

    @Test
    @DisplayName("Crear un cómic y verificar atributos")
    void testCrearComic() {
        assertEquals("Superman", comic.getTitulo());
        assertEquals("DC Comics", comic.getAutor());
        assertEquals(1938, comic.getAñoPublicacion());
        assertEquals("123-456", comic.getIsbn());
        assertEquals("Comic", comic.getGenero());
        assertEquals(1, comic.getVolumen());
        assertEquals("Superhéroes", comic.getDemografia());
    }

    @Test
    @DisplayName("Estados de lectura del cómic")
    void testEstadoLecturaComic() {
        assertEquals(EstadoLectura.PENDIENTE, comic.getEstado());
        comic.setEstado(EstadoLectura.LEYENDO);
        assertEquals(EstadoLectura.LEYENDO, comic.getEstado());
    }
}
