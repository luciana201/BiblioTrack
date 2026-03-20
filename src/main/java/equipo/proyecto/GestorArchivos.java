package main.java.equipo.proyecto;

import java.io.*;
import java.util.List;

public class GestorArchivos {

    public static void guardarBiblioteca(Biblioteca biblioteca, String ruta) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {

            for (Publicacion p : biblioteca.getPublicaciones()) {

                writer.write(p.getTitulo() + ";" +
                             p.getAutor() + ";" +
                             p.getAñoPublicacion() + ";" +
                             p.getIsbn() + ";" +
                             p.getGenero());

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

                String[] datos = linea.split(";");

                String titulo = datos[0];
                String autor = datos[1];
                int año = Integer.parseInt(datos[2]);
                String isbn = datos[3];
                String genero = datos[4];

                Publicacion p = new Comic(titulo, autor, año, isbn, genero,
                                           "Desconocido", 1, "General");

                biblioteca.agregarPublicacion(p);
            }

        } catch (IOException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }

        return biblioteca;
    }
}