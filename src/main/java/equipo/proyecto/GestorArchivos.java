package equipo.proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GestorArchivos {

    public static void guardarBiblioteca(Biblioteca biblioteca, String ruta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            for (Publicacion p : biblioteca.getLibros()) {
                if (p instanceof Comic) {
                    Comic comic = (Comic) p;
                    writer.write("COMIC;" +
                            p.getId() + ";" +
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
                            p.getId() + ";" +
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
                            p.getId() + ";" +
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

    public static Publicacion buscarPorId(String ruta, String idBuscado) {
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            String linea;
            while ((linea = raf.readLine()) != null) {
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
                System.out.println("Linea inválida (datos insuficientes): " + linea);
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
            
            if ("COMIC".equals(tipo) && datos.length >= 11) {
                String ilustrador = datos[8];
                int volumen = Integer.parseInt(datos[9]);
                String demografia = datos[10];
                p = new Comic(id, titulo, autor, año, isbn, genero, ilustrador, volumen, demografia);
            } else if ("NOVELA".equals(tipo) && datos.length >= 10) {
                String tipoNarrador = datos[8];
                int numeroPaginas = Integer.parseInt(datos[9]);
                p = new Novela(id, titulo, autor, año, isbn, genero, tipoNarrador, numeroPaginas);
            } else if ("LIBROTECNICO".equals(tipo) && datos.length >= 10) {
                String tema = datos[8];
                String nivel = datos[9];
                p = new LibroTecnico(id, titulo, autor, año, isbn, genero, tema, nivel);
            } else {
                System.out.println("Tipo de publicación desconocido o datos incompletos: " + tipo);
            }
            if (p != null) {
                p.setEstado(estado);
            }
            return p;
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear datos numéricos: " + linea + " - " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Estado de lectura inválido: " + linea + " - " + e.getMessage());
            return null;
        }
    }
}