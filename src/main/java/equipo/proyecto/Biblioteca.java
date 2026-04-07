package equipo.proyecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Biblioteca {
    private String nombre;
    private ArrayList<Publicacion> publicaciones;
    private HashMap<String, ArrayList<Publicacion>> categorias;
    private HashMap<String, List<Publicacion>> indiceSecundario;
    private ArrayList<Usuario> usuarios;

    public Biblioteca() {
        this.publicaciones = new ArrayList<>();
        this.categorias = new HashMap<>();
        this.indiceSecundario = new HashMap<>();
        this.usuarios = new ArrayList<>();
    }

    public void agregarPublicacion(Publicacion publicacion) {
        if (publicacion != null) {
            publicaciones.add(publicacion);
            String clave = publicacion.getGenero() != null ? publicacion.getGenero().toLowerCase() : "sin_genero";
            indiceSecundario.computeIfAbsent(clave, k -> new ArrayList<>()).add(publicacion);
        }
    }

    public boolean eliminarPublicacion(String titulo) {
        Iterator<Publicacion> it = publicaciones.iterator();
        while (it.hasNext()) {
            Publicacion p = it.next();
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                it.remove();
                eliminarDeIndice(p);
                return true;
            }
        }
        return false;
    }

    private void eliminarDeIndice(Publicacion publicacion) {
        String clave = publicacion.getGenero() != null ? publicacion.getGenero().toLowerCase() : "sin_genero";
        List<Publicacion> lista = indiceSecundario.get(clave);
        if (lista != null) {
            lista.remove(publicacion);
            if (lista.isEmpty()) {
                indiceSecundario.remove(clave);
            }
        }
    }

    public Publicacion buscarPublicacion(String titulo) {
        Pattern pattern = Pattern.compile(Pattern.quote(titulo), Pattern.CASE_INSENSITIVE);
        return publicaciones.stream()
                .filter(p -> pattern.matcher(p.getTitulo()).find())
                .findFirst()
                .orElse(null);
    }

    public List<Publicacion> buscarPorTituloRegex(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return publicaciones.stream()
                .filter(p -> pattern.matcher(p.getTitulo()).find())
                .collect(Collectors.toList());
    }

    public ArrayList<Publicacion> filtrarPorGenero(String genero) {
        return publicaciones.stream()
                .filter(p -> p.getGenero() != null && p.getGenero().equalsIgnoreCase(genero))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Publicacion> filtrarPorEstado(EstadoLectura estado) {
        return publicaciones.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Publicacion> filtrarPorCalificacion(int calificacionMinima) {
        return publicaciones.stream()
                .filter(p -> p.getReseñas().stream()
                        .mapToInt(Reseña::getCalificacion)
                        .average()
                        .orElse(0.0) >= calificacionMinima)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            this.usuarios.add(usuario);
        }
    }

    public boolean eliminarUsuario(String nombre) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombre().equalsIgnoreCase(nombre)) {
                usuarios.remove(i);
                return true;
            }
        }
        return false;
    }

    public Usuario buscarUsuarioID(String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(nombre) || u.getId().equalsIgnoreCase(nombre)) {
                return u;
            }
        }
        return null;
    }

    public boolean agregarReseña(String tituloPublicacion, Reseña reseña) {
        Publicacion p = buscarPublicacion(tituloPublicacion);
        if (p != null && reseña != null) {
            p.agregarReseña(reseña);
            if (reseña.getUsuario() != null) {
                reseña.getUsuario().agregarReseña(reseña);
            }
            return true;
        }
        return false;
    }

    public Publicacion getLibroMejorCalificado() {
        return publicaciones.stream()
                .filter(p -> !p.getReseñas().isEmpty())
                .max(Comparator.comparingDouble(p -> p.getReseñas().stream()
                        .mapToInt(Reseña::getCalificacion)
                        .average()
                        .orElse(0.0)))
                .orElse(null);
    }

    public Publicacion getAutoresMasLeidos() {
        return publicaciones.stream()
                .filter(p -> p.getEstado() == EstadoLectura.LEIDO)
                .max(Comparator.comparingLong(p -> publicaciones.stream()
                        .filter(x -> x.getAutor().equalsIgnoreCase(p.getAutor()))
                        .count()))
                .orElse(null);
    }

    public ArrayList<Publicacion> getLibrosEstado(EstadoLectura estado) {
        return filtrarPorEstado(estado);
    }

    public ArrayList<Publicacion> getLibrosGenero(String genero) {
        return filtrarPorGenero(genero);
    }

    public ArrayList<Publicacion> getLibros() {
        return publicaciones;
    }

    public List<Publicacion> buscarPorGeneroSecundario(String genero) {
        return indiceSecundario.getOrDefault(genero != null ? genero.toLowerCase() : "", new ArrayList<>());
    }

    public ArrayList<Publicacion> getLibrosOrdenadosPorTitulo() {
        return publicaciones.stream()
                .sorted(Comparator.comparing(Publicacion::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Map<String, List<Publicacion>> getPublicacionesAgrupadasPorGenero() {
        return publicaciones.stream()
                .collect(Collectors.groupingBy(p -> p.getGenero() != null ? p.getGenero().toLowerCase() : "sin_genero"));
    }

    public double getPromedioCalificacionGeneral() {
        return publicaciones.stream()
                .flatMap(p -> p.getReseñas().stream())
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0.0);
    }
}
