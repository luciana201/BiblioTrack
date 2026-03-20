package equipo.proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitarios para Usuario")
public class UsuarioTest {
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan Pérez", "jPerez", "juan@example.com");
    }

    @Test
    @DisplayName("Crear y modificar usuario")
    void testUsuario() {
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("jPerez", usuario.getId());
        assertEquals("juan@example.com", usuario.getEmail());

        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@example.com");
        assertEquals("Carlos", usuario.getNombre());
        assertEquals("carlos@example.com", usuario.getEmail());
    }
}
