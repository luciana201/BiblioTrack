package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PanelDetalleLibro extends JPanel {

    private Publicacion publicacion;
    private Usuario usuarioActual;
    private Biblioteca biblioteca;
    private JLabel labelEstado;
    private MainFrame mainFrame;

    public PanelDetalleLibro(Publicacion pub, Usuario usuario,
            Biblioteca bib, JLabel estado, MainFrame frame) {
        this.publicacion  = pub;
        this.usuarioActual = usuario;
        this.biblioteca   = bib;
        this.labelEstado  = estado;
        this.mainFrame    = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }

    private void initUI() {
        // ── NORTE ──
        JPanel topContainer = new JPanel(new BorderLayout(10, 10));

        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> mainFrame.volverInicio());
        topContainer.add(btnVolver, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new BorderLayout(14, 0));

        JPanel portada = new JPanel();
        portada.setPreferredSize(new Dimension(90, 130));
        portada.setBackground(new Color(0xE87722));
        topPanel.add(portada, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(
            new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(publicacion.getTitulo());
        lblTitulo.setFont(
            new Font("SansSerif", Font.BOLD, 16));

        JLabel lblAutor = new JLabel(
            "Autor: " + publicacion.getAutor());
        lblAutor.setForeground(Color.GRAY);

        JLabel lblGenero = new JLabel(
            "Género: " + publicacion.getGenero());

        JLabel lblInfo = new JLabel(
            publicacion.getInfoDetallada());
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(0x666666));

        // Estado PERSONAL del usuario
        EstadoLectura estadoPersonal =
            usuarioActual.getEstadoLibro(publicacion.getId());
        JLabel lblEstadoLectura = new JLabel(
            "Estado: " + obtenerTextoEstado(estadoPersonal));

        double media = mediaDePublicacion(publicacion);
        JLabel lblRating = new JLabel(media == 0
            ? "Sin puntuaciones aún"
            : String.format("%.1f/10  %s",
                media, estrellas(media)));
        lblRating.setForeground(new Color(0xE87722));
        lblRating.setFont(
            new Font("SansSerif", Font.BOLD, 13));

        JButton btnCambiarEstado = new JButton(
            "Cambiar Estado");
        btnCambiarEstado.setBackground(new Color(0xE87722));
        btnCambiarEstado.setForeground(Color.WHITE);
        btnCambiarEstado.setFocusPainted(false);
        btnCambiarEstado.setAlignmentX(
            Component.LEFT_ALIGNMENT);
        btnCambiarEstado.addActionListener(e ->
            cambiarEstadoLectura(lblEstadoLectura));

        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblAutor);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblGenero);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblInfo);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblEstadoLectura);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblRating);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(btnCambiarEstado);

        topPanel.add(infoPanel, BorderLayout.CENTER);
        topContainer.add(topPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // ── CENTRO: reseñas + formulario ──
        JPanel centroPanel = new JPanel();
        centroPanel.setLayout(
            new BoxLayout(centroPanel, BoxLayout.Y_AXIS));

        JPanel listaResenas = new JPanel();
        listaResenas.setLayout(
            new BoxLayout(listaResenas, BoxLayout.Y_AXIS));
        listaResenas.setBorder(
            BorderFactory.createTitledBorder("Reseñas"));
        actualizarListaResenas(listaResenas);

        JPanel formResena = new JPanel(
            new BorderLayout(6, 6));
        formResena.setBorder(
            BorderFactory.createTitledBorder(
                "Tu reseña — " +
                usuarioActual.getNombre()));

        JSpinner spinnerCal = new JSpinner(
            new SpinnerNumberModel(8, 1, 10, 1));
        JTextArea txtComentario = new JTextArea(3, 20);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);

        JButton btnGuardar = new JButton("Guardar reseña");
        btnGuardar.setBackground(new Color(0xE87722));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JPanel calRow = new JPanel(
            new FlowLayout(FlowLayout.LEFT));
        calRow.add(new JLabel("Calificación (1-10):"));
        calRow.add(spinnerCal);

        formResena.add(calRow, BorderLayout.NORTH);
        formResena.add(new JScrollPane(txtComentario),
            BorderLayout.CENTER);
        formResena.add(btnGuardar, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String comentario = txtComentario.getText().trim();
            if (comentario.isEmpty()) {
                labelEstado.setText(
                        " Escribe un comentario.");
                return;
            }
            int cal = (int) spinnerCal.getValue();
            Reseña nueva = new Reseña(
                    comentario, cal,
                    usuarioActual, publicacion);
           biblioteca.agregarReseña(publicacion.getTitulo(), nueva);

            GestorArchivos.guardarDatos(biblioteca, AppGui.RUTA_CSV, AppGui.RUTA_USUARIOS, AppGui.RUTA_JSON);

            labelEstado.setText(
                    " Reseña guardada para: " +
                            publicacion.getTitulo());
            txtComentario.setText("");
            actualizarListaResenas(listaResenas);
            listaResenas.revalidate();
            listaResenas.repaint();
        });

        centroPanel.add(listaResenas);
        centroPanel.add(Box.createVerticalStrut(10));
        centroPanel.add(formResena);
        add(new JScrollPane(centroPanel),
            BorderLayout.CENTER);

        // ── SUR: ranking del género ──
        List<Publicacion> mismoGenero =
            biblioteca.getPublicacionesPorGenero(
                publicacion.getGenero());
        mismoGenero.sort((a, b) ->
            Double.compare(mediaDePublicacion(b),mediaDePublicacion(a)));

        String[] cols = {"#", "Título", "Autor", "Media"};
        DefaultTableModel modelo =
            new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(
                    int r, int c) { return false; }
            };

        int pos = 1;
        for (Publicacion p : mismoGenero) {
            modelo.addRow(new Object[]{
                pos++, p.getTitulo(), p.getAutor(),
                String.format("%.1f",
                    mediaDePublicacion(p)) + "/10"
            });
        }

        JTable tablaRanking = new JTable(modelo);
        tablaRanking.setRowHeight(24);
        JScrollPane scrollRanking =
            new JScrollPane(tablaRanking);
        scrollRanking.setBorder(
            BorderFactory.createTitledBorder(
                "Ranking — " + publicacion.getGenero()));
        scrollRanking.setPreferredSize(
            new Dimension(0, 160));
        add(scrollRanking, BorderLayout.SOUTH);
    }

    private void cambiarEstadoLectura(
            JLabel lblEstadoLectura) {
        String[] opciones = {
            "Sin Leer", "En Proceso", "Leído"};
        EstadoLectura actual =
            usuarioActual.getEstadoLibro(
                publicacion.getId());
        int idx = actual == EstadoLectura.LEYENDO ? 1
                : actual == EstadoLectura.LEIDO   ? 2 : 0;

        String seleccion = (String)
            JOptionPane.showInputDialog(
                this,
                "Nuevo estado de lectura:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[idx]);

        if (seleccion != null) {
            EstadoLectura nuevo =
                "En Proceso".equals(seleccion)
                    ? EstadoLectura.LEYENDO
                : "Leído".equals(seleccion)
                    ? EstadoLectura.LEIDO
                : EstadoLectura.PENDIENTE;

            usuarioActual.setEstadoLibro(
                publicacion.getId(), nuevo);
            lblEstadoLectura.setText(
                "Estado: " + obtenerTextoEstado(nuevo));

            GestorArchivos.guardarDatos(biblioteca,
                "data/biblioteca/biblioteca.csv",
                "data/usuarios/usuarios.csv",
                "data/biblioteca/biblioteca.json");

            labelEstado.setText(
                " Estado actualizado.");
        }
    }

    private String obtenerTextoEstado(
            EstadoLectura estado) {
        if (estado == null) return "Sin Leer";
        switch (estado) {
            case PENDIENTE: return "Sin Leer";
            case LEYENDO:   return "En Proceso";
            case LEIDO:     return "Leído";
            default:        return "Sin Leer";
        }
    }

    private double mediaDePublicacion(Publicacion pub) {
        return pub.getReseñas().stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0);
    }

    private String estrellas(double media10) {
        int n = (int) Math.round(media10 / 2.0);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++)
            sb.append(i <= n ? "★" : "☆");
        return sb.toString();
    }

    private void actualizarListaResenas(JPanel panel) {
        panel.removeAll();
        DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Reseña> todas = publicacion.getReseñas();

        if (todas.isEmpty()) {
            panel.add(new JLabel(
                "  Aún no hay reseñas."));
        }
        for (Reseña r : todas) {
            JLabel lbl = new JLabel("<html><b>"
                + r.getUsuario().getNombre()
                + "</b> — "
                + r.getCalificacion() + "/10 · "
                + r.getFecha().format(fmt)
                + "<br><i>" + r.getComentario()
                + "</i></html>");
            lbl.setBorder(
                BorderFactory.createEmptyBorder(
                    4, 6, 4, 6));
            panel.add(lbl);
            panel.add(new JSeparator());
        }
    }
}