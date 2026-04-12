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
    private ArrayList<Publicacion> publicaciones;
    private HashMap<String, List<Publicacion>> indiceSecundario;
    private ArrayList<Usuario> usuarios;

    public Biblioteca() {
        this.publicaciones = new ArrayList<>();
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
        return buscarUsuario(nombre);
    }

    public Usuario buscarUsuario(String identificador) {
        if (identificador == null || identificador.isBlank()) {
            return null;
        }
        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(identificador)
                    || u.getId().equalsIgnoreCase(identificador)
                    || u.getEmail().equalsIgnoreCase(identificador)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
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
                .collect(
                        Collectors.groupingBy(p -> p.getGenero() != null ? p.getGenero().toLowerCase() : "sin_genero"));
    }

    public double getPromedioCalificacionGeneral() {
        return publicaciones.stream()
                .flatMap(p -> p.getReseñas().stream())
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0.0);
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // Usuarios
        sb.append("  \"usuarios\": [\n");
        List<Usuario> listaUsuarios = new ArrayList<>(usuarios);
        for (int i = 0; i < listaUsuarios.size(); i++) {
            String userJson = listaUsuarios.get(i).toJson();
            // Indentar cada línea del objeto usuario
            String indentado = "    " + userJson.replace("{", "{\n      ")
                    .replace("}", "\n    }")
                    .replace("\",\"", "\",\n      \"");
            sb.append(indentado);
            if (i < listaUsuarios.size() - 1)
                sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Publicaciones
        sb.append("  \"publicaciones\": [\n");
        List<Publicacion> lista = new ArrayList<>(publicaciones);
        for (int i = 0; i < lista.size(); i++) {
            String pubJson = lista.get(i).toJson();
            String indentado = "    " + pubJson
                    .replace("{", "{\n      ")
                    .replace("}", "\n    }")
                    .replace("\",\"", "\",\n      \"")
                    .replace("\",\"reseñas\":", "\",\n      \"reseñas\":")
                    .replace("],", "],\n      ")
                    .replace("[{", "[\n        {")
                    .replace("},{", "},\n        {")
                    .replace("}]", "}\n      ]");
            sb.append(indentado);
            if (i < lista.size() - 1)
                sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");

        sb.append("}");
        return sb.toString();
    }

    public List<Publicacion> getPublicacionesPorGenero(String genero) {
        return filtrarPorGenero(genero);
    }
}
