package equipo.proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para GestorArchivos (I/O)")
public class GestorArchivosTest {
    private Biblioteca biblioteca;
    private Comic comic;
    private Novela novela;
    private LibroTecnico libroTecnico;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Comic",
                "Joe Shuster", 1, "Superhéroes");
        novela = new Novela("Cien años de soledad", "Gabriel García Márquez", 1967,
                "789-012", "Novela", "Omnisciente", 400);
        libroTecnico = new LibroTecnico("Clean Code", "Robert Martin", 2008,
                "345-678", "Libro Técnico", "Desarrollo de Software", "Intermedio");

        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(novela);
        biblioteca.agregarPublicacion(libroTecnico);
    }

    @Test
    @DisplayName("Guardar y cargar biblioteca desde archivo temporal")
    void testGuardarYCargar() {
        String ruta = "test_biblioteca_tmp.txt";
        GestorArchivos.guardarBiblioteca(biblioteca, ruta);
        Biblioteca cargada = GestorArchivos.cargarBiblioteca(ruta);
        assertNotNull(cargada);
        assertEquals(3, cargada.getLibros().size());

        java.io.File f = new java.io.File(ruta);
        if (f.exists()) f.delete();
    }
}
