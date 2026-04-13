package equipo.proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestorArchivos {
    public static void guardarBiblioteca(Biblioteca biblioteca, String ruta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            for (Publicacion p : biblioteca.getLibros()) {
                String base = p.getId() + ";" + p.getTitulo() + ";" + p.getAutor() + ";" +
                        p.getAñoPublicacion() + ";" + p.getIsbn() + ";" +
                        p.getGenero() + ";" + p.getEstado();

                if (p instanceof Comic) {
                    Comic c = (Comic) p;
                    writer.write("COMIC;" + base + ";" + c.getVolumen() + ";" + c.getDemografia());
                } else if (p instanceof Novela) {
                    Novela n = (Novela) p;
                    writer.write("NOVELA;" + base + ";" + n.getNumeroPaginas());
                } else if (p instanceof LibroTecnico) {
                    LibroTecnico l = (LibroTecnico) p;
                    writer.write("LIBROTECNICO;" + base + ";" + l.getTema() + ";" + l.getNivel());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
    public static Biblioteca cargarBiblioteca(String ruta) {
        Biblioteca biblioteca = new Biblioteca();
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Publicacion p = parsePublicacion(linea);
                if (p != null) {
                    biblioteca.agregarPublicacion(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
        return biblioteca;
    }

    public static void guardarUsuariosCsv(Biblioteca biblioteca, String ruta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            for (Usuario u : biblioteca.getUsuarios()) {
                writer.write(u.getId() + ";" + u.getNombre() + ";" + u.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static void cargarUsuariosCsv(Biblioteca biblioteca, String ruta) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 3) {
                    Usuario usuario = new Usuario(datos[1], datos[0], datos[2]);
                    biblioteca.agregarUsuario(usuario);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar usuarios: " + e.getMessage());
        }
    }

    public static void guardarDatos(Biblioteca biblioteca, String rutaPublicaciones, String rutaUsuarios, String rutaJson) {
        guardarBiblioteca(biblioteca, rutaPublicaciones);
        guardarUsuariosCsv(biblioteca, rutaUsuarios);
        guardarBibliotecaJson(biblioteca, rutaJson);
    }

    public static Biblioteca cargarDatos(String rutaPublicaciones, String rutaUsuarios, String rutaJson) {
        if (Files.exists(Paths.get(rutaJson))) {
            return cargarBibliotecaJson(rutaJson); // ya devuelve una biblioteca completa
        }
        Biblioteca biblioteca = new Biblioteca();
        cargarUsuariosCsv(biblioteca, rutaUsuarios);
        cargarBiblioteca(rutaPublicaciones).getLibros()
                .forEach(biblioteca::agregarPublicacion);
        return biblioteca;
    }
    public static void guardarBibliotecaJson(Biblioteca biblioteca, String ruta) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ruta), StandardCharsets.UTF_8)) {
            writer.write(biblioteca.toJson());
            System.out.println("Exportación JSON completada: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    public static Biblioteca cargarBibliotecaJson(String ruta) {
        Biblioteca biblioteca = new Biblioteca();
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(ruta)), StandardCharsets.UTF_8);
            String usuariosJson = getJsonArray(contenido, "usuarios");
            if (usuariosJson != null) {
                for (String usuarioJson : splitJsonArrayObjects(usuariosJson)) {
                    String id = getJsonString(usuarioJson, "id");
                    String nombre = getJsonString(usuarioJson, "nombre");
                    String email = getJsonString(usuarioJson, "email");
                    if (nombre != null && id != null && email != null) {
                        biblioteca.agregarUsuario(new Usuario(nombre, id, email));
                    }
                }
            }
            String publicacionesJson = getJsonArray(contenido, "publicaciones");
            if (publicacionesJson != null) {
                for (String publicacionJson : splitJsonArrayObjects(publicacionesJson)) {
                    Publicacion p = parsePublicacionJson(publicacionJson, biblioteca);
                    if (p != null) {
                        biblioteca.agregarPublicacion(p);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar JSON: " + e.getMessage());
        }
        return biblioteca;
    }

    private static String getJsonArray(String json, String key) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) return null;
        int start = json.indexOf('[', matcher.end());
        if (start < 0) return null;
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '[') depth++;
            else if (c == ']') {
                depth--;
                if (depth == 0) return json.substring(start + 1, i);
            }
        }
        return null;
    }

    private static List<String> splitJsonArrayObjects(String arrayContent) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    objects.add(arrayContent.substring(start, i + 1));
                }
            }
        }
        return objects;
    }

    private static String getJsonString(String json, String key) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*\\\"(.*?)\\\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? unescapeJson(matcher.group(1)) : null;
    }

    private static int getJsonInt(String json, String key, int defaultValue) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : defaultValue;
    }

    private static String unescapeJson(String value) {
        return value.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\\\", "\\");
    }

    private static Publicacion parsePublicacionJson(String json, Biblioteca biblioteca) {
        String tipo = getJsonString(json, "tipo");
        String id = getJsonString(json, "id");
        String titulo = getJsonString(json, "titulo");
        String autor = getJsonString(json, "autor");
        int año = getJsonInt(json, "añoPublicacion", 0);
        String isbn = getJsonString(json, "isbn");
        String genero = getJsonString(json, "genero");
        String estadoValor = getJsonString(json, "estado");

        Publicacion p = null;

        // Publicacion(id, tipo, titulo, ...) — orden del constructor de Publicacion
        if ("Comic".equalsIgnoreCase(tipo)) {
            int volumen = getJsonInt(json, "volumen", 0);
            String demografia = getJsonString(json, "demografia");
            p = new Comic(id, tipo, titulo, autor, año, isbn, genero, volumen, demografia);

        } else if ("Novela".equalsIgnoreCase(tipo)) {
            int paginas = getJsonInt(json, "numeroPaginas", 0);
            p = new Novela(id, tipo, titulo, autor, año, isbn, genero, paginas);

        } else if ("Libro Técnico".equalsIgnoreCase(tipo) || "LibroTecnico".equalsIgnoreCase(tipo)) {
            String tema = getJsonString(json, "tema");
            String nivel = getJsonString(json, "nivel");
            p = new LibroTecnico(id, tipo, titulo, autor, año, isbn, genero, tema, nivel);
        }

        if (p != null && estadoValor != null) {
            try {
                p.setEstado(EstadoLectura.valueOf(estadoValor));
            } catch (IllegalArgumentException ignored) {}

            String reseñasJson = getJsonArray(json, "reseñas");
            if (reseñasJson != null) {
                for (String reseñaJson : splitJsonArrayObjects(reseñasJson)) {
                    String usuarioNombre = getJsonString(reseñaJson, "usuario");
                    String comentario = getJsonString(reseñaJson, "comentario");
                    int calificacion = getJsonInt(reseñaJson, "calificacion", 0);
                    Usuario usuario = biblioteca.buscarUsuario(usuarioNombre);
                    if (usuario == null) {
                        usuario = new Usuario(usuarioNombre,
                            usuarioNombre.replaceAll("\\s+", "").toLowerCase(), "");
                        biblioteca.agregarUsuario(usuario);
                    }
                    Reseña reseña = new Reseña(comentario, calificacion, usuario, p);
                    p.agregarReseña(reseña);
                    usuario.agregarReseña(reseña);
                }
            }
        }
        return p;
    }

    public static Publicacion buscarPorId(String ruta, String idBuscado) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length > 1 && datos[1].equals(idBuscado)) {
                    return parsePublicacion(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar por id: " + e.getMessage());
        }
        return null;
    }

    private static Publicacion parsePublicacion(String linea) {
        try {
            String[] datos = linea.split(";");
            if (datos.length < 8) {
                System.out.println("Linea inválida: " + linea);
                return null;
            }
            String tipo = datos[0];
            String id = datos[1];
            String titulo = datos[2];
            String autor = datos[3];
            int año = Integer.parseInt(datos[4]);
            String isbn = datos[5];
            String genero = datos[6];
            EstadoLectura estado = EstadoLectura.valueOf(datos[7]);
            Publicacion p = null;

            if ("COMIC".equals(tipo) && datos.length >= 10) {
                int volumen = Integer.parseInt(datos[8]);
                String demografia = datos[9];
                p = new Comic(id, tipo, titulo, autor, año, isbn, genero, volumen, demografia);
            } else if ("NOVELA".equals(tipo) && datos.length >= 9) {
                int numeroPaginas = Integer.parseInt(datos[8]);
                p = new Novela(id, tipo, titulo, autor, año, isbn, genero, numeroPaginas);
            } else if ("LIBROTECNICO".equals(tipo) && datos.length >= 10) {
                String tema = datos[8];
                String nivel = datos[9];
                p = new LibroTecnico(id, tipo, titulo, autor, año, isbn, genero, tema, nivel);
            } else {
                System.out.println("Tipo desconocido: " + tipo);
            }

            if (p != null) p.setEstado(estado);
            return p;

        } catch (NumberFormatException e) {
            System.out.println("Error numérico: " + linea + " - " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Estado inválido: " + linea + " - " + e.getMessage());
            return null;
        }
    }
}