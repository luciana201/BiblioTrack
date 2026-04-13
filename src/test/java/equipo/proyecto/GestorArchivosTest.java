package equipo.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests para GestorArchivos (I/O)")
public class GestorArchivosTest {
    private Biblioteca biblioteca;
    private Comic comic;
    private Novela novela;
    private LibroTecnico libroTecnico;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
        comic = new Comic("Superman",  "Comic", "DC Comics", 1938, "123-456", "Comic", 1, "Superhéroes");
        novela = new Novela("Cien años de soledad", "Novela", "Gabriel García Márquez", 1967,"789-012", "Novela", 400);
        libroTecnico = new LibroTecnico("Clean Code", "Libro Técnico", "Robert Martin", 2008,
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

    @Test
    @DisplayName("Buscar publicación por id usando RandomAccessFile")
    void testBuscarPorId() {
        String ruta = "test_biblioteca_id.txt";
        GestorArchivos.guardarBiblioteca(biblioteca, ruta);
        String idBuscado = biblioteca.getLibros().get(0).getId();
        Publicacion encontrado = GestorArchivos.buscarPorId(ruta, idBuscado);
        assertNotNull(encontrado);
        assertEquals(biblioteca.getLibros().get(0).getTitulo(), encontrado.getTitulo());

        java.io.File f = new java.io.File(ruta);
        if (f.exists()) f.delete();
    }

    @Test
    @DisplayName("Guardar y cargar biblioteca en JSON con usuarios y reseñas")
    void testGuardarYCargarJson() {
        String ruta = "test_biblioteca_tmp.json";
        Usuario usuario = new Usuario("Ana", "ana01", "ana@example.com");
        biblioteca.agregarUsuario(usuario);
        Reseña reseña = new Reseña("Excelente", 9, usuario, comic);
        biblioteca.agregarReseña(comic.getTitulo(), reseña);
        GestorArchivos.guardarBibliotecaJson(biblioteca, ruta);

        Biblioteca cargada = GestorArchivos.cargarBibliotecaJson(ruta);
        assertNotNull(cargada);
        assertEquals(3, cargada.getLibros().size());
        assertEquals(1, cargada.getUsuarios().size());
        assertNotNull(cargada.buscarUsuario("ana01"));
        assertNotNull(cargada.buscarPublicacion("Superman"));
        assertEquals(1, cargada.buscarPublicacion("Superman").getReseñas().size());

        java.io.File f = new java.io.File(ruta);
        if (f.exists()) f.delete();
    }
}
