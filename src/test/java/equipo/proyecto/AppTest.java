/*package test.java.equipo.proyecto;

import equipo.proyecto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test de la aplicación BiblioTrack")
public class AppTest {

    private Biblioteca biblioteca;
    private Comic comic;
    private Novela novela;
    private LibroTecnico libroTecnico;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Acción",
                "Joe Shuster", 1, "Superhéroes");
        novela = new Novela("Cien años de soledad", "Gabriel García Márquez", 1967,
                "789-012", "Realismo Mágico", "Omnisciente", 400);
        libroTecnico = new LibroTecnico("Clean Code", "Robert C. Martin", 2008,
                "345-678", "Programación", "Desarrollo de Software", "Intermedio");
        usuario = new Usuario("Juan Pérez", "juan@example.com");
    }

    // ===== TESTS PARA COMIC =====
    @Test
    @DisplayName("Crear un cómic y verificar atributos")
    void testCrearComic() {
        assertEquals("Superman", comic.getTitulo());
        assertEquals("DC Comics", comic.getAutor());
        assertEquals(1938, comic.getAñoPublicacion());
        assertEquals("123-456", comic.getIsbn());
        assertEquals("Acción", comic.getGenero());
        assertEquals("Joe Shuster", comic.getIlustrador());
        assertEquals(1, comic.getVolumen());
        assertEquals("Superhéroes", comic.getDemografia());
    }

    @Test
    @DisplayName("Obtener información detallada del cómic")
    void testInfoDetalladaComic() {
        String info = comic.getInfoDetallada();
        assertTrue(info.contains("Joe Shuster"));
        assertTrue(info.contains("1"));
        assertTrue(info.contains("Superhéroes"));
    }

    @Test
    @DisplayName("Modificar atributos del cómic")
    void testModificarComic() {
        comic.setIlustrador("Alex Ross");
        comic.setVolumen(5);
        comic.setDemografia("Adultos");

        assertEquals("Alex Ross", comic.getIlustrador());
        assertEquals(5, comic.getVolumen());
        assertEquals("Adultos", comic.getDemografia());
    }

    @Test
    @DisplayName("Estado de lectura del cómic")
    void testEstadoLecturaComic() {
        assertEquals(EstadoLectura.PENDIENTE, comic.getEstado());
        comic.setEstado(EstadoLectura.LEYENDO);
        assertEquals(EstadoLectura.LEYENDO, comic.getEstado());
    }

    // ===== TESTS PARA NOVELA =====
    @Test
    @DisplayName("Crear una novela y verificar atributos")
    void testCrearNovela() {
        assertEquals("Cien años de soledad", novela.getTitulo());
        assertEquals("Gabriel García Márquez", novela.getAutor());
        assertEquals(1967, novela.getAñoPublicacion());
        assertEquals("Realismo Mágico", novela.getGenero());
        assertEquals("Omnisciente", novela.getTipoNarrador());
        assertEquals(400, novela.getNumeroPaginas());
    }

    @Test
    @DisplayName("Obtener información detallada de la novela")
    void testInfoDetalladaNovela() {
        String info = novela.getInfoDetallada();
        assertTrue(info.contains("Omnisciente"));
        assertTrue(info.contains("400"));
    }

    @Test
    @DisplayName("Modificar atributos de la novela")
    void testModificarNovela() {
        novela.setTipoNarrador("Primera persona");
        novela.setNumeroPaginas(450);

        assertEquals("Primera persona", novela.getTipoNarrador());
        assertEquals(450, novela.getNumeroPaginas());
    }

    // ===== TESTS PARA LIBRO TÉCNICO =====
    @Test
    @DisplayName("Crear un libro técnico y verificar atributos")
    void testCrearLibroTecnico() {
        assertEquals("Clean Code", libroTecnico.getTitulo());
        assertEquals("Robert C. Martin", libroTecnico.getAutor());
        assertEquals(2008, libroTecnico.getAñoPublicacion());
        assertEquals("Programación", libroTecnico.getGenero());
        assertEquals("Desarrollo de Software", libroTecnico.getTema());
        assertEquals("Intermedio", libroTecnico.getNivel());
    }

    @Test
    @DisplayName("Obtener información detallada del libro técnico")
    void testInfoDetalladaLibroTecnico() {
        String info = libroTecnico.getInfoDetallada();
        assertTrue(info.contains("Desarrollo de Software"));
        assertTrue(info.contains("Intermedio"));
    }

    @Test
    @DisplayName("Modificar atributos del libro técnico")
    void testModificarLibroTecnico() {
        libroTecnico.setTema("Algoritmos");
        libroTecnico.setNivel("Avanzado");

        assertEquals("Algoritmos", libroTecnico.getTema());
        assertEquals("Avanzado", libroTecnico.getNivel());
    }

    // ===== TESTS PARA BIBLIOTECA =====
    @Test
    @DisplayName("Crear una biblioteca vacía")
    void testCrearBibliotecaVacia() {
        Biblioteca bibVacia = new Biblioteca();
        assertNotNull(bibVacia.getLibros());
        assertEquals(0, bibVacia.getLibros().size());
    }

    @Test
    @DisplayName("Agregar publicaciones a la biblioteca")
    void testAgregarPublicaciones() {
        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(novela);
        biblioteca.agregarPublicacion(libroTecnico);

        assertEquals(3, biblioteca.getLibros().size());
        assertTrue(biblioteca.getLibros().contains(comic));
        assertTrue(biblioteca.getLibros().contains(novela));
        assertTrue(biblioteca.getLibros().contains(libroTecnico));
    }

    @Test
    @DisplayName("Agregar múltiples cómics")
    void testAgregarMultiplesComics() {
        Comic comic2 = new Comic("Batman", "DC Comics", 1939, "111-222", "Acción", "Bob Kane", 2, "Superhéroes");
        Comic comic3 = new Comic("Spider-Man", "Marvel", 1962, "333-444", "Acción", "Steve Ditko", 3, "Superhéroes");

        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(comic2);
        biblioteca.agregarPublicacion(comic3);

        assertEquals(3, biblioteca.getLibros().size());
    }

    // ===== TESTS PARA USUARIO =====
    @Test
    @DisplayName("Crear un usuario")
    void testCrearUsuario() {
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan@example.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Modificar datos del usuario")
    void testModificarUsuario() {
        usuario.setNombre("Carlos López");
        usuario.setEmail("carlos@example.com");

        assertEquals("Carlos López", usuario.getNombre());
        assertEquals("carlos@example.com", usuario.getEmail());
    }

    // ===== TESTS PARA RESEÑA =====
    @Test
    @DisplayName("Crear una reseña")
    void testCrearResena() {
        Reseña resena = new Reseña("Excelente cómic, muy emocionante", 9, usuario, comic);

        assertEquals("Excelente cómic, muy emocionante", resena.getComentario());
        assertEquals(9, resena.getCalificacion());
        assertEquals(usuario, resena.getUsuario());
        assertEquals(comic, resena.getPublicacion());
        assertNotNull(resena.getFecha());
    }

    @Test
    @DisplayName("Validar rango de calificación")
    void testCalificacionRango() {
        Reseña resena = new Reseña("Muy bueno", 10, usuario, novela);
        assertEquals(10, resena.getCalificacion());

        Reseña resena2 = new Reseña("No me gustó", 1, usuario, libroTecnico);
        assertEquals(1, resena2.getCalificacion());
    }

    @Test
    @DisplayName("Modificar comentario de reseña")
    void testModificarResena() {
        Reseña resena = new Reseña("Buen libro", 7, usuario, comic);
        resena.setComentario("Excelente libro");
        assertEquals("Excelente libro", resena.getComentario());
    }

    @Test
    @DisplayName("String de reseña")
    void testToStringResena() {
        Reseña resena = new Reseña("Genial", 8, usuario, comic);
        String str = resena.toString();
        assertTrue(str.contains("Juan Pérez"));
        assertTrue(str.contains("Superman"));
        assertTrue(str.contains("Genial"));
        assertTrue(str.contains("8"));
    }

    // ===== TESTS PARA RESEÑAS EN PUBLICACIÓN =====
    @Test
    @DisplayName("Agregar reseñas a una publicación")
    void testAgregarResenaAPublicacion() {
        Reseña resena1 = new Reseña("Excelente", 9, usuario, comic);
        Reseña resena2 = new Reseña("Muy bueno", 8, usuario, comic);

        comic.agregarReseña(resena1);
        comic.agregarReseña(resena2);

        assertEquals(2, comic.getReseñas().size());
    }

    // ===== TESTS PARA GESTOR DE ARCHIVOS =====
    @Test
    @DisplayName("Guardar y cargar biblioteca")
    void testGuardarYCargarBiblioteca() {
        // Preparar
        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(novela);
        biblioteca.agregarPublicacion(libroTecnico);

        String rutaTemp = "test_biblioteca.txt";

        // Guardar
        GestorArchivos.guardarBiblioteca(biblioteca, rutaTemp);

        // Cargar
        Biblioteca bibliotecaCargada = GestorArchivos.cargarBiblioteca(rutaTemp);

        // Verificar
        assertNotNull(bibliotecaCargada);
        assertEquals(3, bibliotecaCargada.getLibros().size());

        // Limpiar
        java.io.File archivo = new java.io.File(rutaTemp);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    @Test
    @DisplayName("Guardar biblioteca y verificar tipo de publicación")
    void testGuardarAndVerificarTipo() {
        biblioteca.agregarPublicacion(comic);
        String rutaTemp = "test_comic.txt";

        GestorArchivos.guardarBiblioteca(biblioteca, rutaTemp);
        Biblioteca bibliotecaCargada = GestorArchivos.cargarBiblioteca(rutaTemp);

        assertEquals(1, bibliotecaCargada.getLibros().size());
        Publicacion p = bibliotecaCargada.getLibros().get(0);
        assertInstanceOf(Comic.class, p);

        Comic comicCargado = (Comic) p;
        assertEquals("Superman", comicCargado.getTitulo());
        assertEquals("Joe Shuster", comicCargado.getIlustrador());

        // Limpiar
        java.io.File archivo = new java.io.File(rutaTemp);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

}
    */
