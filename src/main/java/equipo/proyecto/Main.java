package main.java.equipo.proyecto;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DE BIBL1OTRACK ===\n");

        // Test 1: Crear Comic
        System.out.println("TEST 1: Crear un Cómic");
        Comic comic = new Comic("Superman", "DC Comics", 1938, "123-456", "Acción",
                "Joe Shuster", 1, "Superhéroes");
        assert "Superman".equals(comic.getTitulo()) : "Título incorrecto";
        assert "Joe Shuster".equals(comic.getIlustrador()) : "Ilustrador incorrecto";
        assert comic.getVolumen() == 1 : "Volumen incorrecto";
        System.out.println("Comic creado correctamente");
        System.out.println("  Info: " + comic.getInfoDetallada() + "\n");

        // Test 2: Crear Novela
        System.out.println("TEST 2: Crear una Novela");
        Novela novela = new Novela("Cien años de soledad", "Gabriel García Márquez", 1967,
                "789-012", "Realismo Mágico", "Omnisciente", 400);
        assert "Cien años de soledad".equals(novela.getTitulo()) : "Título incorrecto";
        assert "Omnisciente".equals(novela.getTipoNarrador()) : "Narrador incorrecto";
        assert novela.getNumeroPaginas() == 400 : "Páginas incorrectas";
        System.out.println("Novela creada correctamente");
        System.out.println("  Info: " + novela.getInfoDetallada() + "\n");

        // Test 3: Crear LibroTecnico
        System.out.println("TEST 3: Crear un Libro Técnico");
        LibroTecnico libroTecnico = new LibroTecnico("Clean Code", "Robert C. Martin", 2008,
                "345-678", "Programación", "Desarrollo de Software", "Intermedio");
        assert "Clean Code".equals(libroTecnico.getTitulo()) : "Título incorrecto";
        assert "Desarrollo de Software".equals(libroTecnico.getTema()) : "Tema incorrecto";
        assert "Intermedio".equals(libroTecnico.getNivel()) : "Nivel incorrecto";
        System.out.println("Libro Técnico creado correctamente");
        System.out.println("  Info: " + libroTecnico.getInfoDetallada() + "\n");

        // Test 4: Estados de lectura
        System.out.println("TEST 4: Estados de Lectura");
        assert comic.getEstado() == EstadoLectura.PENDIENTE : "Estado inicial debe ser PENDIENTE";
        comic.setEstado(EstadoLectura.LEYENDO);
        assert comic.getEstado() == EstadoLectura.LEYENDO : "Estado debería ser LEYENDO";
        comic.setEstado(EstadoLectura.LEIDO);
        assert comic.getEstado() == EstadoLectura.LEIDO : "Estado debería ser LEIDO";
        System.out.println("Estados funcionan correctamente\n");

        // Test 5: Crear Usuario
        System.out.println("TEST 5: Crear Usuario");
        Usuario usuario = new Usuario("Juan Pérez", "juan@example.com");
        assert "Juan Pérez".equals(usuario.getNombre()) : "Nombre incorrecto";
        assert "juan@example.com".equals(usuario.getEmail()) : "Email incorrecto";
        System.out.println("Usuario creado correctamente\n");

        // Test 6: Crear Reseña
        System.out.println("TEST 6: Crear Reseña");
        Reseña resena = new Reseña("Excelente cómic, muy emocionante", 9, usuario, comic);
        assert "Excelente cómic, muy emocionante".equals(resena.getComentario()) : "Comentario incorrecto";
        assert resena.getCalificacion() == 9 : "Calificación incorrecta";
        assert usuario.equals(resena.getUsuario()) : "Usuario incorrecto";
        assert comic.equals(resena.getPublicacion()) : "Publicación incorrecta";
        System.out.println("Reseña creada correctamente");
        System.out.println("  " + resena.toString() + "\n");

        // Test 7: Agregar reseña a publicación
        System.out.println("TEST 7: Agregar Reseña a Publicación");
        comic.agregarReseña(resena);
        Reseña resena2 = new Reseña("Muy bueno", 8, usuario, comic);
        comic.agregarReseña(resena2);
        assert comic.getReseñas().size() == 2 : "Debería haber 2 reseñas";
        System.out.println("Reseñas agregadas a publicación");
        System.out.println("  Total de reseñas: " + comic.getReseñas().size() + "\n");

        // Test 8: Crear Biblioteca
        System.out.println("TEST 8: Crear Biblioteca");
        Biblioteca biblioteca = new Biblioteca();
        assert biblioteca.getLibros() != null : "Lista de libros no debe ser null";
        assert biblioteca.getLibros().size() == 0 : "Biblioteca debe estar vacía";
        System.out.println("Biblioteca creada correctamente\n");

        // Test 9: Agregar publicaciones a biblioteca
        System.out.println("TEST 9: Agregar Publicaciones a Biblioteca");
        biblioteca.agregarPublicacion(comic);
        biblioteca.agregarPublicacion(novela);
        biblioteca.agregarPublicacion(libroTecnico);
        assert biblioteca.getLibros().size() == 3 : "Debería haber 3 publicaciones";
        System.out.println("Publicaciones agregadas correctamente");
        System.out.println("  Total de publicaciones: " + biblioteca.getLibros().size() + "\n");

        // Test 10: Guardar y cargar en archivo
        System.out.println("TEST 10: Guardar y Cargar Biblioteca");
        String rutaTest = "test_biblioteca.txt";
        GestorArchivos.guardarBiblioteca(biblioteca, rutaTest);
        System.out.println("Biblioteca guardada");

        Biblioteca bibliotecaCargada = GestorArchivos.cargarBiblioteca(rutaTest);
        assert bibliotecaCargada.getLibros().size() == 3 : "Debería haber 3 publicaciones cargadas";
        System.out.println("Biblioteca cargada correctamente");
        System.out.println("  Publicaciones cargadas: " + bibliotecaCargada.getLibros().size() + "\n");

        // Test 11: Verificar tipos cargados
        System.out.println("TEST 11: Verificar Tipos de Publicaciones Cargadas");
        int comicsCount = 0;
        int novelasCount = 0;
        int librosCount = 0;

        for (Publicacion p : bibliotecaCargada.getLibros()) {
            if (p instanceof Comic) comicsCount++;
            else if (p instanceof Novela) novelasCount++;
            else if (p instanceof LibroTecnico) librosCount++;
        }

        assert comicsCount == 1 : "Debería haber 1 Comic";
        assert novelasCount == 1 : "Debería haber 1 Novela";
        assert librosCount == 1 : "Debería haber 1 Libro Técnico";
        System.out.println("Tipos de publicaciones correctos");
        System.out.println("  Comics: " + comicsCount);
        System.out.println("  Novelas: " + novelasCount);
        System.out.println("  Libros Técnicos: " + librosCount + "\n");

        // Test 12: Verificar datos de Comic cargado
        System.out.println("TEST 12: Verificar Datos del Comic Cargado");
        Comic comicCargado = null;
        for (Publicacion p : bibliotecaCargada.getLibros()) {
            if (p instanceof Comic) {
                comicCargado = (Comic) p;
                break;
            }
        }
        assert comicCargado != null : "Comic no encontrado";
        assert "Superman".equals(comicCargado.getTitulo()) : "Título incorrecto";
        assert "Joe Shuster".equals(comicCargado.getIlustrador()) : "Ilustrador incorrecto";
        assert comicCargado.getVolumen() == 1 : "Volumen incorrecto";
        System.out.println("Datos del Comic cargados correctamente");
        System.out.println("  Título: " + comicCargado.getTitulo());
        System.out.println("  Ilustrador: " + comicCargado.getIlustrador());
        System.out.println("  Volumen: " + comicCargado.getVolumen() + "\n");

        // Limpiar archivo de prueba
        java.io.File archivo = new java.io.File(rutaTest);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
