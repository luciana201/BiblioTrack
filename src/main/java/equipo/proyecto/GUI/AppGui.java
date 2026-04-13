package equipo.proyecto.GUI;

import equipo.proyecto.Biblioteca;
import equipo.proyecto.GestorArchivos;

public class AppGui {
    public static final String RUTA_CSV = "data/biblioteca/biblioteca.csv";
    public static final String RUTA_USUARIOS = "data/usuarios/usuarios.csv";
    public static final String RUTA_JSON = "data/biblioteca/biblioteca.json";

    public static void main(String[] args) {
        Biblioteca biblioteca = GestorArchivos.cargarDatos(
                RUTA_CSV, RUTA_USUARIOS, RUTA_JSON);
        new LoginDialog(null, biblioteca).setVisible(true);
    }
}