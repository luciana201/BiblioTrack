package equipo.proyecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Biblioteca {
    private ArrayList<Publicacion> publicaciones;
    private HashMap<String, List<Publicacion>> indiceSecundario;
    private ArrayList<Usuario> usuarios;

    public Biblioteca() {
        this.publicaciones = new ArrayList<>();
        this.indiceSecundario = new HashMap<>(); // indice para buscar por género
        this.usuarios = new ArrayList<>();
    }

    private String normalizar(String s) {
        return s.toLowerCase()
                .replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("á", "a")
                .replace("ú", "u").replace(" ", "");
    }

    public void agregarPublicacion(Publicacion publicacion) {
        if (publicacion != null) {
            publicaciones.add(publicacion);
            String clave = publicacion.getGenero() != null ? publicacion.getGenero().toLowerCase() : "sin_genero"; // si no tiene genero se guarda con clave "sin_genero"
            indiceSecundario.computeIfAbsent(clave, k -> new ArrayList<>()).add(publicacion); // se agrega a otro indice
                                                                                              // para bucarlo por genero
                                                                                              // asi no se recorre todo
                                                                                              // de vuelta, y se busca
                                                                                              // directo en este indice
        }
    }

    private void eliminarDeIndice(Publicacion publicacion) {// elimina la puvlicacon solo del indice secundario
        String clave = publicacion.getGenero() != null ? publicacion.getGenero().toLowerCase() : "sin_genero";
        List<Publicacion> lista = indiceSecundario.get(clave);
        if (lista != null) {
            lista.remove(publicacion);
            if (lista.isEmpty()) {
                indiceSecundario.remove(clave);
            }
        }
    }

    public boolean eliminarPublicacion(String titulo) {
        Iterator<Publicacion> it = publicaciones.iterator();
        while (it.hasNext()) {// se recorre toda la lista
            Publicacion p = it.next();
            if (p.getTitulo().equalsIgnoreCase(titulo)) {// si coincide se elimina del indice principal y del secundario
                it.remove();
                eliminarDeIndice(p);
                return true;
            }
        }
        return false;
    }

    public Publicacion buscarPublicacion(String titulo) {  // para buscarlo de forma mas flexible
        Pattern pattern = Pattern.compile(Pattern.quote(titulo), Pattern.CASE_INSENSITIVE); // ya sea cn todo el titulo o parcial 
        return publicaciones.stream() // se recorre toda la lista de publicaciones
                .filter(p -> pattern.matcher(p.getTitulo()).find()) 
                .findFirst()// devuelve la primera que coincida
                .orElse(null);
    }

    public List<Publicacion> buscarPorTituloRegex(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return publicaciones.stream()
                .filter(p -> pattern.matcher(p.getTitulo()).find())
                .collect(Collectors.toList()); //devuelve una lista
    }

    public List<Publicacion> filtrarPorTipo(String tipo, int limite) {
        return getLibros().stream()
                .filter(p -> normalizar(p.getTipo()).equals(normalizar(tipo)))
                .limit(limite)
                .collect(Collectors.toList());

    }

    public List<Publicacion> filtrarPorGenero(String genero) {
        String clave = genero != null ? genero.toLowerCase() : "sin_genero";
        return indiceSecundario.getOrDefault(clave, new ArrayList<>());
    }

    public ArrayList<Publicacion> filtrarPorEstado(EstadoLectura estado) {
        return publicaciones.stream()
                .filter(p -> p.getEstado() == estado) // se filtra por el estado de lectura
                .collect(Collectors.toCollection(ArrayList::new));//genera lista nueva con las q coincidan 
    }

    public ArrayList<Publicacion> filtrarPorCalificacion(int calificacionMinima) { //por implementar 
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

    public Usuario buscarUsuario(String identificador) {// A las finales no se usa, pero serviria para buscar por email
                                                        // o nombre
                                                        // genial para el futuro
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

    public List<Usuario> getUsuarios() { // para obtener lista de usuarios
        return usuarios;
    }

    public boolean agregarReseña(String tituloPublicacion, Reseña reseña) { // se agrega reseña a la publi 
        Publicacion p = buscarPublicacion(tituloPublicacion);
        if (p != null && reseña != null) {
            p.agregarReseña(reseña);
            if (reseña.getUsuario() != null) {
                reseña.getUsuario().agregarReseña(reseña); // se agrega la reseña al usuario, para qu haya como un registo
            }
            return true;
        }
        return false;
    }

    public List<Publicacion> getLibroMejorCalificados(int limite) { // devuelve los libros con mejor calificacion promedio
        List<Publicacion> mejorCalificados = new ArrayList<>(publicaciones);
        mejorCalificados.sort((a, b) -> Double.compare(
                b.getReseñas().stream().mapToInt(Reseña::getCalificacion).average().orElse(0), 
                a.getReseñas().stream().mapToInt(Reseña::getCalificacion).average().orElse(0)));
        return mejorCalificados.stream().limit(limite)
                .collect(Collectors.toList());
    }

    public Publicacion getAutoresMasLeidos() { //No se llego a implementar
        return publicaciones.stream()
                .filter(p -> p.getEstado() == EstadoLectura.LEIDO)
                .max(Comparator.comparingLong(p -> publicaciones.stream()
                        .filter(x -> x.getAutor().equalsIgnoreCase(p.getAutor()))
                        .count()))
                .orElse(null);
    }

    public ArrayList<Publicacion> getLibros() {
        return publicaciones;
    }

    public ArrayList<Publicacion> getLibrosOrdenadosPorTitulo() {
        return publicaciones.stream()
                .sorted(Comparator.comparing(Publicacion::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(ArrayList::new));
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
