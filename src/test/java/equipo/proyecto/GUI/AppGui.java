package equipo.proyecto.GUI;

import equipo.proyecto.Biblioteca;
import equipo.proyecto.GestorArchivos;

public class AppGui {
    public static void main(String[] args) {
        Biblioteca biblioteca = GestorArchivos.cargarDatos(
            "data/biblioteca/biblioteca.csv",
            "data/usuarios/usuarios.csv",
            "data/biblioteca/biblioteca.json"
        );
        new LoginDialog(null, biblioteca).setVisible(true);
    }
}