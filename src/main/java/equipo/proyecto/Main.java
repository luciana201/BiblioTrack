package equipo.proyecto;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern PATTERN_TEXTO = Pattern.compile("^[\\p{L}0-9 .,'-]{2,100}$");
    private static final Pattern PATTERN_ISBN = Pattern.compile("^\\d{3}-\\d{3}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PATTERN_YEAR = Pattern.compile("^(19|20)\\d{2}$");
    private static final Pattern PATTERN_RATING = Pattern.compile("^([1-9]|10)$");

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        try (Scanner scanner = new Scanner(System.in)) {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero(scanner, "Seleccione una opción", Pattern.compile("^[1-5]$"));
                switch (opcion) {
                    case 1:
                        agregarPublicacion(scanner, biblioteca);
                        break;
                    case 2:
                        mostrarPublicaciones(biblioteca);
                        break;
                    case 3:
                        agregarReseña(scanner, biblioteca);
                        break;
                    case 4:
                        mostrarReseñas(biblioteca);
                        break;
                    case 5:
                        System.out.println("Saliendo de BiblioTrack...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } while (opcion != 5);
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.printf("%-3s %-25s%n", "No", "Descripción");
        System.out.printf("%-3s %-25s%n", "1", "Agregar publicación");
        System.out.printf("%-3s %-25s%n", "2", "Ver lista de publicaciones");
        System.out.printf("%-3s %-25s%n", "3", "Agregar reseña");
        System.out.printf("%-3s %-25s%n", "4", "Ver reseñas");
        System.out.printf("%-3s %-25s%n", "5", "Salir");
    }

    private static void agregarPublicacion(Scanner scanner, Biblioteca biblioteca) {
        String tipo = leerTexto(scanner, "Tipo (COMIC/NOVELA/LIBROTECNICO)", Pattern.compile("^(COMIC|NOVELA|LIBROTECNICO)$", Pattern.CASE_INSENSITIVE));
        String titulo = leerTexto(scanner, "Título", PATTERN_TEXTO);
        String autor = leerTexto(scanner, "Autor", PATTERN_TEXTO);
        String isbn = leerTexto(scanner, "ISBN (XXX-XXX)", PATTERN_ISBN);
        int año = Integer.parseInt(leerTexto(scanner, "Año de publicación (1900-2099)", PATTERN_YEAR));
        String genero = leerTexto(scanner, "Género", PATTERN_TEXTO);

        Publicacion publicacion;
        if (tipo.equalsIgnoreCase("COMIC")) {
            String ilustrador = leerTexto(scanner, "Ilustrador", PATTERN_TEXTO);
            int volumen = Integer.parseInt(leerTexto(scanner, "Volumen", Pattern.compile("^[1-9]\\\d*$")));
            String demografia = leerTexto(scanner, "Demografía", PATTERN_TEXTO);
            publicacion = new Comic(titulo, autor, año, isbn, genero, ilustrador, volumen, demografia);
        } else if (tipo.equalsIgnoreCase("NOVELA")) {
            String narrador = leerTexto(scanner, "Tipo de narrador", PATTERN_TEXTO);
            int paginas = Integer.parseInt(leerTexto(scanner, "Número de páginas", Pattern.compile("^[1-9]\\\d*$")));
            publicacion = new Novela(titulo, autor, año, isbn, genero, narrador, paginas);
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
        String nombre = leerTexto(scanner, "Nombre del usuario", PATTERN_TEXTO);
        String email = leerTexto(scanner, "Email", PATTERN_EMAIL);
        String comentario = leerTexto(scanner, "Comentario (5-200 caracteres)", Pattern.compile("^.{5,200}$"));
        int calificacion = Integer.parseInt(leerTexto(scanner, "Calificación (1-10)", PATTERN_RATING));
        Usuario usuario = new Usuario(nombre, nombre.replaceAll("\\s+", "").toLowerCase(), email);
        Reseña reseña = new Reseña(comentario, calificacion, usuario, publicacion);
        biblioteca.agregarReseña(titulo, reseña);
        System.out.println("Reseña agregada correctamente.");
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
