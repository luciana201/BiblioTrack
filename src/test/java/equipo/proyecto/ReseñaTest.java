package equipo.proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitarios para Reseña")
public class ReseñaTest {
    private Usuario usuario;
    private Comic comic;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan Pérez", "jPerez", "juan@example.com");
        comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Comic",
                "Joe Shuster", 1, "Superhéroes");
    }

    @Test
    @DisplayName("Crear y validar reseña")
    void testResena() {
        Reseña r = new Reseña("Excelente", 9, usuario, comic);
        assertEquals("Excelente", r.getComentario());
        assertEquals(9, r.getCalificacion());
        assertEquals(usuario, r.getUsuario());
        assertEquals(comic, r.getPublicacion());
        assertNotNull(r.getFecha());
    }
}
