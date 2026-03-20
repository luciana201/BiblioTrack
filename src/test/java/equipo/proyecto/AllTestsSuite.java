package equipo.proyecto;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ComicTest.class,
    NovelaTest.class,
    LibroTecnicoTest.class,
    UsuarioTest.class,
    ReseñaTest.class,
    BibliotecaUnitTest.class,
    GestorArchivosTest.class
})
public class AllTestsSuite {

}
