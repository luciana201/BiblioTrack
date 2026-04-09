package equipo.proyecto;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests unitarios para Biblioteca")
public class BibliotecaUnitTest {
    private Biblioteca biblioteca;
    private Comic comic;
    private Novela novela;
    private LibroTecnico libroTecnico;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Comic", 1, "Superhéroes");
        novela = new Novela("Cien años de soledad", "Gabriel García Márquez", 1967, "789-012", "Novela", 400);
        libroTecnico = new LibroTecnico("Clean Code", "Robert Martin", 2008,
                "345-678", "Libro Técnico", "Desarrollo de Software", "Intermedio");
        usuario = new Usuario("Juan Pérez", "jPerez", "juan@example.com");

        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(novela);
        biblioteca.agregarPublicacion(libroTecnico);
        biblioteca.agregarUsuario(usuario);
    }

    @Test
    @DisplayName("Buscar y eliminar publicación")
    void testBuscarYEliminar() {
        assertNotNull(biblioteca.buscarPublicacion("Superman"));
        assertTrue(biblioteca.eliminarPublicacion("Superman"));
        assertNull(biblioteca.buscarPublicacion("Superman"));
    }

    @Test
    @DisplayName("Filtrar por género y estado")
    void testFiltrar() {
        ArrayList<Publicacion> comics = biblioteca.filtrarPorGenero("Comic");
        assertTrue(comics.contains(comic));

        comic.setEstado(EstadoLectura.LEIDO);
        ArrayList<Publicacion> leidos = biblioteca.filtrarPorEstado(EstadoLectura.LEIDO);
        assertTrue(leidos.contains(comic));
    }

    @Test
    @DisplayName("Filtrar por calificación mínima y mejores")
    void testCalificacionYMejor() {
        biblioteca.agregarReseña(comic.getTitulo(), new Reseña("Ok", 8, usuario, comic));
        biblioteca.agregarReseña(novela.getTitulo(), new Reseña("Obra", 10, usuario, novela));

        ArrayList<Publicacion> buenos = biblioteca.filtrarPorCalificacion(9);
        assertTrue(buenos.contains(novela));

        Publicacion mejor = biblioteca.getLibroMejorCalificado();
        assertNotNull(mejor);
        assertEquals(novela.getTitulo(), mejor.getTitulo());
    }

    @Test
    @DisplayName("Búsqueda y agrupación con streams y expresiones regulares")
    void testStreamsYRegex() {
        biblioteca.agregarReseña(comic.getTitulo(), new Reseña("Ok", 8, usuario, comic));
        biblioteca.agregarReseña(novela.getTitulo(), new Reseña("Obra", 10, usuario, novela));

        assertFalse(biblioteca.buscarPorTituloRegex("Super").isEmpty());
        assertEquals(1, biblioteca.buscarPorGeneroSecundario("Comic").size());
        assertEquals(3, biblioteca.getPublicacionesAgrupadasPorGenero().size());
        assertEquals(9.0, biblioteca.getPromedioCalificacionGeneral(), 0.1);
    }
}
