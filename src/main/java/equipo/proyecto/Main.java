package equipo.proyecto;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern PATTERN_TEXTO = Pattern.compile("^[\\p{L}0-9 .,'-]{2,100}$");
    private static final Pattern PATTERN_ISBN = Pattern.compile("^\\d{3}-\\d{3}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PATTERN_YEAR = Pattern.compile("^(0\\d{3}|1\\d{3}|20[0-8]\\d|2090)$");
    private static final Pattern PATTERN_RATING = Pattern.compile("^([1-9]|10)$");
    private static final Pattern PATTERN_ID = Pattern.compile("^[a-zA-Z0-9_-]{1,20}$");
    private static final String RUTA_PUBLICACIONES_CSV = "data/biblioteca/biblioteca.csv";
    private static final String RUTA_USUARIOS_CSV = "data/usuarios/usuarios.csv";
    private static final String RUTA_BIBLIOTECA_JSON = "data/biblioteca/biblioteca.json";

    private static Usuario usuarioActual = null;

    public static void main(String[] args) {
        Biblioteca biblioteca = GestorArchivos.cargarDatos(RUTA_PUBLICACIONES_CSV, RUTA_USUARIOS_CSV, RUTA_BIBLIOTECA_JSON);
        try (Scanner scanner = new Scanner(System.in)) {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero(scanner, "Seleccione una opción", Pattern.compile("^(?:[1-9]|10)$"));
                switch (opcion) {
                    case 1:
                        agregarPublicacion(scanner, biblioteca);
                        GestorArchivos.guardarDatos(biblioteca, RUTA_PUBLICACIONES_CSV, RUTA_USUARIOS_CSV, RUTA_BIBLIOTECA_JSON);
                        break;
                    case 2:
                        mostrarPublicaciones(biblioteca);
                        break;
                    case 3:
                        if (usuarioActual == null) {
                            System.out.println("Debe iniciar sesión para agregar reseñas.");
                            break;
                        }
                        agregarReseña(scanner, biblioteca);
                        GestorArchivos.guardarDatos(biblioteca, RUTA_PUBLICACIONES_CSV, RUTA_USUARIOS_CSV, RUTA_BIBLIOTECA_JSON);
                        break;
                    case 4:
                        mostrarReseñas(biblioteca);
                        break;
                    case 5:
                        registrarUsuario(scanner, biblioteca);
                        GestorArchivos.guardarDatos(biblioteca, RUTA_PUBLICACIONES_CSV, RUTA_USUARIOS_CSV, RUTA_BIBLIOTECA_JSON);
                        break;
                    case 6:
                        iniciarSesion(scanner, biblioteca);
                        break;
                    case 7:
                        cerrarSesion();
                        break;
                    case 8:
                        exportarBibliotecaJson(scanner, biblioteca);
                        break;
                    case 9:
                        biblioteca = importarBibliotecaJson(scanner);
                        GestorArchivos.guardarDatos(biblioteca, RUTA_PUBLICACIONES_CSV, RUTA_USUARIOS_CSV, RUTA_BIBLIOTECA_JSON);
                        break;
                    case 10:
                        mostrarUsuarios(biblioteca);
                        break;
                }
            } while (opcion != 10);
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.printf("%-3s %-35s%n", "No", "Descripción");
        System.out.printf("%-3s %-35s%n", "1", "Agregar publicación");
        System.out.printf("%-3s %-35s%n", "2", "Ver lista de publicaciones");
        System.out.printf("%-3s %-35s%n", "3", "Agregar reseña");
        System.out.printf("%-3s %-35s%n", "4", "Ver reseñas");
        System.out.printf("%-3s %-35s%n", "5", "Registrar usuario");
        System.out.printf("%-3s %-35s%n", "6", "Iniciar sesión");
        System.out.printf("%-3s %-35s%n", "7", "Cerrar sesión");
        System.out.printf("%-3s %-35s%n", "8", "Exportar biblioteca a JSON");
        System.out.printf("%-3s %-35s%n", "9", "Importar biblioteca desde JSON");
        System.out.printf("%-3s %-35s%n", "10", "Mostrar usuarios registrados");
        if (usuarioActual != null) {
            System.out.println("Usuario actual: " + usuarioActual.getNombre() + " (" + usuarioActual.getId() + ")");
        } else {
            System.out.println("No hay usuario logueado.");
        }
    }

    private static void agregarPublicacion(Scanner scanner, Biblioteca biblioteca) {
    
        String tipo = leerTexto(scanner, "Tipo (COMIC/NOVELA/LIBROTECNICO)", Pattern.compile("^(COMIC|NOVELA|LIBROTECNICO)$", Pattern.CASE_INSENSITIVE));
        String titulo = leerTexto(scanner, "Título", PATTERN_TEXTO);
        String autor = leerTexto(scanner, "Autor", PATTERN_TEXTO);
        String isbn = leerTexto(scanner, "ISBN (XXX-XXX)", PATTERN_ISBN);
        int año = Integer.parseInt(leerTexto(scanner, "Año de publicación (0-2099)", PATTERN_YEAR));
        String genero = leerTexto(scanner, "Género", PATTERN_TEXTO);

        Publicacion publicacion;
        if (tipo.equalsIgnoreCase("COMIC")) {
            int volumen = Integer.parseInt(leerTexto(scanner, "Volumen", Pattern.compile("^[1-9]\\d*$")));
            String demografia = leerTexto(scanner, "Demografía", PATTERN_TEXTO);
            publicacion = new Comic(titulo, autor, año, isbn, genero, volumen, demografia);
        } else if (tipo.equalsIgnoreCase("NOVELA")) {
            int paginas = Integer.parseInt(leerTexto(scanner, "Número de páginas", Pattern.compile("^[1-9]\\d*$")));
            publicacion = new Novela( titulo, autor, año, isbn, genero, paginas);
        } else {
            String tema = leerTexto(scanner, "Tema", PATTERN_TEXTO);
            String nivel = leerTexto(scanner, "Nivel", PATTERN_TEXTO);
            publicacion = new LibroTecnico(titulo, autor, año, isbn, genero, tema, nivel);
        }

        biblioteca.agregarPublicacion(publicacion);
        System.out.println("Publicación agregada correctamente.");
    }

    private static void mostrarPublicaciones(Biblioteca biblioteca) {
        System.out.printf("%-5s %-25s %-20s %-10s %-8s%n", "ID", "Título", "Autor", "Año", "Estado");
        for (Publicacion p : biblioteca.getLibrosOrdenadosPorTitulo()) {
            System.out.printf("%-5s %-25s %-20s %-10d %-8s%n",
                    p.getId(), p.getTitulo(), p.getAutor(), p.getAñoPublicacion(), p.getEstado());
        }
    }

    private static void agregarReseña(Scanner scanner, Biblioteca biblioteca) {
        if (biblioteca.getLibros().isEmpty()) {
            System.out.println("No hay publicaciones registradas.");
            return;
        }
        String titulo = leerTexto(scanner, "Título de la publicación", PATTERN_TEXTO);
        Publicacion publicacion = biblioteca.buscarPublicacion(titulo);
        if (publicacion == null) {
            System.out.println("No se encontró la publicación.");
            return;
        }
        Usuario usuario = buscarUsuarioRegistrado(scanner, biblioteca);
        if (usuario == null) {
            return;
        }
        String comentario = leerTexto(scanner, "Comentario (5-200 caracteres)", Pattern.compile("^.{5,200}$"));
        int calificacion = Integer.parseInt(leerTexto(scanner, "Calificación (1-10)", PATTERN_RATING));
        Reseña reseña = new Reseña(comentario, calificacion, usuario, publicacion);
        biblioteca.agregarReseña(titulo, reseña);
        System.out.println("Reseña agregada correctamente.");
    }

    private static Usuario registrarUsuario(Scanner scanner, Biblioteca biblioteca) {
        String id = leerTexto(scanner, "ID del usuario", PATTERN_ID);
        if (biblioteca.buscarUsuario(id) != null) {
            System.out.println("Ya existe un usuario con ese ID.");
            return null;
        }
        String nombre = leerTexto(scanner, "Nombre del usuario", PATTERN_TEXTO);
        String email = leerTexto(scanner, "Email", PATTERN_EMAIL);
        if (biblioteca.buscarUsuario(nombre) != null || biblioteca.buscarUsuario(email) != null) {
            System.out.println("Ya existe un usuario registrado con ese nombre o correo.");
            return null;
        }
        Usuario usuario = new Usuario(nombre, id, email);
        biblioteca.agregarUsuario(usuario);
        System.out.println("Usuario registrado correctamente. ID: " + usuario.getId());
        return usuario;
    }

    private static Usuario buscarUsuarioRegistrado(Scanner scanner, Biblioteca biblioteca) {
        String identificador = leerTexto(scanner, "Nombre, id o email del usuario", PATTERN_TEXTO);
        Usuario usuario = biblioteca.buscarUsuario(identificador);
        if (usuario != null) {
            return usuario;
        }
        System.out.println("Usuario no encontrado. ¿Deseas registrar uno nuevo? (S/N)");
        String respuesta = scanner.nextLine().trim();
        if (respuesta.equalsIgnoreCase("S")) {
            return registrarUsuario(scanner, biblioteca);
        }
        return null;
    }

    private static void iniciarSesion(Scanner scanner, Biblioteca biblioteca) {
        if (usuarioActual != null) {
            System.out.println("Ya hay un usuario logueado. Cierra sesión primero.");
            return;
        }
        String identificador = leerTexto(scanner, "ID, nombre o email del usuario", PATTERN_TEXTO);
        Usuario usuario = biblioteca.buscarUsuario(identificador);
        if (usuario != null) {
            usuarioActual = usuario;
            System.out.println("Sesión iniciada como: " + usuario.getNombre());
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private static void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada para: " + usuarioActual.getNombre());
            usuarioActual = null;
        } else {
            System.out.println("No hay usuario logueado.");
        }
    }

    private static void exportarBibliotecaJson(Scanner scanner, Biblioteca biblioteca) {
        String ruta = leerTexto(scanner, "Ruta de exportación JSON", PATTERN_TEXTO);
        GestorArchivos.guardarBibliotecaJson(biblioteca, ruta);
    }

    private static Biblioteca importarBibliotecaJson(Scanner scanner) {
        String ruta = leerTexto(scanner, "Ruta de importación JSON", PATTERN_TEXTO);
        Biblioteca biblioteca = GestorArchivos.cargarBibliotecaJson(ruta);
        System.out.println("Importación completada desde: " + ruta);
        return biblioteca;
    }

    private static void mostrarUsuarios(Biblioteca biblioteca) {
        List<Usuario> usuarios = biblioteca.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.printf("%-10s %-20s %-30s%n", "ID", "Nombre", "Email");
        for (Usuario u : usuarios) {
            System.out.printf("%-10s %-20s %-30s%n", u.getId(), u.getNombre(), u.getEmail());
        }
    }

    private static void mostrarReseñas(Biblioteca biblioteca) {
        List<Publicacion> libros = biblioteca.getLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay publicaciones registradas.");
            return;
        }
        for (Publicacion p : libros) {
            System.out.println("Publicación: " + p.getTitulo());
            if (p.getReseñas().isEmpty()) {
                System.out.println("  Sin reseñas.");
            } else {
                for (Reseña r : p.getReseñas()) {
                    System.out.printf("  - %s (%d/10) por %s%n", r.getComentario(), r.getCalificacion(), r.getUsuario().getNombre());
                }
            }
        }
    }

    private static String leerTexto(Scanner scanner, String etiqueta, Pattern patron) {
        String texto;
        do {
            System.out.print(etiqueta + ": ");
            texto = scanner.nextLine().trim();
            if (!patron.matcher(texto).matches()) { 
                System.out.println("Valor inválido. Intenta de nuevo.");
            }
        } while (!patron.matcher(texto).matches());
        return texto;
    }

    private static int leerEntero(Scanner scanner, String etiqueta, Pattern patron) {
        String texto;
        do {
            System.out.print(etiqueta + ": ");
            texto = scanner.nextLine().trim();
            if (!patron.matcher(texto).matches()) {
                System.out.println("Valor inválido. Intenta de nuevo.");
            }
        } while (!patron.matcher(texto).matches());

        return Integer.parseInt(texto);
    }
}
