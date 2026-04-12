package equipo.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests unitarios para Novela")
public class NovelaTest {
    private Novela novela;

    @BeforeEach
    void setUp() {
        novela = new Novela("Cien años de soledad",  "Novela", "Gabriel García Márquez", 1967,
                "789-012", "Novela", 400);
    }

    @Test
    @DisplayName("Crear una novela y verificar atributos")
    void testCrearNovela() {
        assertEquals("Cien años de soledad", novela.getTitulo());
        assertEquals("Gabriel García Márquez", novela.getAutor());
        assertEquals(1967, novela.getAñoPublicacion());
        assertEquals("Novela", novela.getGenero());
        assertEquals(400, novela.getNumeroPaginas());
    }
}
