package main.java.equipo.proyecto;
import java.io.*;

public class GestorArchivos {

    public static void guardarBiblioteca(Biblioteca biblioteca, String ruta) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {

            for (Publicacion p : biblioteca.getLibros()) {

                // Formato: TIPO;titulo;autor;año;isbn;genero;estado;campos específicos
                if (p instanceof Comic) {
                    Comic comic = (Comic) p;
                    writer.write("COMIC;" +
                                 p.getTitulo() + ";" +
                                 p.getAutor() + ";" +
                                 p.getAñoPublicacion() + ";" +
                                 p.getIsbn() + ";" +
                                 p.getGenero() + ";" +
                                 p.getEstado() + ";" +
                                 comic.getIlustrador() + ";" +
                                 comic.getVolumen() + ";" +
                                 comic.getDemografia());

                } else if (p instanceof Novela) {
                    Novela novela = (Novela) p;
                    writer.write("NOVELA;" +
                                 p.getTitulo() + ";" +
                                 p.getAutor() + ";" +
                                 p.getAñoPublicacion() + ";" +
                                 p.getIsbn() + ";" +
                                 p.getGenero() + ";" +
                                 p.getEstado() + ";" +
                                 novela.getTipoNarrador() + ";" +
                                 novela.getNumeroPaginas());

                } else if (p instanceof LibroTecnico) {
                    LibroTecnico libro = (LibroTecnico) p;
                    writer.write("LIBROTECNICO;" +
                                 p.getTitulo() + ";" +
                                 p.getAutor() + ";" +
                                 p.getAñoPublicacion() + ";" +
                                 p.getIsbn() + ";" +
                                 p.getGenero() + ";" +
                                 p.getEstado() + ";" +
                                 libro.getTema() + ";" +
                                 libro.getNivel());
                }

                writer.newLine();
            }

            System.out.println("Biblioteca guardada correctamente");

        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }


    public static Biblioteca cargarBiblioteca(String ruta) {

        Biblioteca biblioteca = new Biblioteca();

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {

            String linea;

            while ((linea = reader.readLine()) != null) {

                try {
                    String[] datos = linea.split(";");

                    if (datos.length < 7) {
                        System.out.println("Linea inválida (datos insuficientes): " + linea);
                        continue;
                    }

                    String tipo = datos[0];
                    String titulo = datos[1];
                    String autor = datos[2];
                    int año = Integer.parseInt(datos[3]);
                    String isbn = datos[4];
                    String genero = datos[5];
                    EstadoLectura estado = EstadoLectura.valueOf(datos[6]);

                    Publicacion p = null;

                    if ("COMIC".equals(tipo) && datos.length >= 10) {
                        String ilustrador = datos[7];
                        int volumen = Integer.parseInt(datos[8]);
                        String demografia = datos[9];
                        p = new Comic(titulo, autor, año, isbn, genero, ilustrador, volumen, demografia);

                    } else if ("NOVELA".equals(tipo) && datos.length >= 9) {
                        String tipoNarrador = datos[7];
                        int numeroPaginas = Integer.parseInt(datos[8]);
                        p = new Novela(titulo, autor, año, isbn, genero, tipoNarrador, numeroPaginas);

                    } else if ("LIBROTECNICO".equals(tipo) && datos.length >= 9) {
                        String tema = datos[7];
                        String nivel = datos[8];
                        p = new LibroTecnico(titulo, autor, año, isbn, genero, tema, nivel);

                    } else {
                        System.out.println("Tipo de publicación desconocido o datos incompletos: " + tipo);
                        continue;
                    }

                    if (p != null) {
                        p.setEstado(estado);
                        biblioteca.agregarPublicacion(p);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear datos numéricos: " + linea + " - " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Estado de lectura inválido: " + linea + " - " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }

        return biblioteca;
    }
}