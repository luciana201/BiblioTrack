package equipo.proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitarios para LibroTecnico")
public class LibroTecnicoTest {
    private LibroTecnico libro;

    @BeforeEach
    void setUp() {
        libro = new LibroTecnico("Clean Code", "Robert Martin", 2008,
                "345-678", "Libro Técnico", "Desarrollo de Software", "Intermedio");
    }

    @Test
    @DisplayName("Crear libro técnico y verificar atributos")
    void testCrearLibroTecnico() {
        assertEquals("Clean Code", libro.getTitulo());
        assertEquals("Robert Martin", libro.getAutor());
        assertEquals(2008, libro.getAñoPublicacion());
        assertEquals("Libro Técnico", libro.getGenero());
        assertEquals("Desarrollo de Software", libro.getTema());
        assertEquals("Intermedio", libro.getNivel());
    }
}
