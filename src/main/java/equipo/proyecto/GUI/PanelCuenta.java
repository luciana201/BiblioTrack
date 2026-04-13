package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelCuenta extends JPanel {

    private Biblioteca biblioteca;
    private Usuario usuarioActual;
    private MainFrame mainFrame;

    private JTabbedPane tabbedPane;
    private JTable tablaPendientes;
    private JTable tablaLeyendo;
    private JTable tablaLeidos;

    public PanelCuenta(Biblioteca biblioteca, Usuario usuario,
                       MainFrame mainFrame) {
        this.biblioteca = biblioteca;
        this.usuarioActual = usuario;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }

    private void initUI() {
        JPanel panelUsuario = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelUsuario.setBorder(
            BorderFactory.createTitledBorder("Mi Cuenta"));

        JLabel lblNombre = new JLabel(
            "Usuario: " + usuarioActual.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelUsuario.add(lblNombre);

        JLabel lblEmail = new JLabel(
            "Email: " + usuarioActual.getEmail());
        panelUsuario.add(lblEmail);

        JLabel lblId = new JLabel(
            "ID: " + usuarioActual.getId());
        lblId.setForeground(Color.GRAY);
        panelUsuario.add(lblId);

        add(panelUsuario, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        tablaPendientes = crearTabla();
        tabbedPane.addTab("Sin Leer",
            new JScrollPane(tablaPendientes));

        tablaLeyendo = crearTabla();
        tabbedPane.addTab("En Proceso",
            new JScrollPane(tablaLeyendo));

        tablaLeidos = crearTabla();
        tabbedPane.addTab("Leídos",
            new JScrollPane(tablaLeidos));

        add(tabbedPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnCambiarEstado = new JButton(
            "Cambiar Estado de Lectura");
        btnCambiarEstado.setBackground(new Color(0xE87722));
        btnCambiarEstado.setForeground(Color.WHITE);
        btnCambiarEstado.setFocusPainted(false);
        btnCambiarEstado.addActionListener(
            e -> cambiarEstadoSeleccionado());
        panelBotones.add(btnCambiarEstado);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarDatos());
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private JTable crearTabla() {
        String[] columnas = {
            "ID", "Tipo", "Título", "Autor", "Género", "Año", "Estado"
        };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(
                    java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int fila = tabla.getSelectedRow();
                    if (fila >= 0) {
                        String titulo = (String)
                            modelo.getValueAt(fila, 2);
                        Publicacion pub =
                            biblioteca.buscarPublicacion(titulo);
                        if (pub != null)
                            mainFrame.abrirDetalle(pub);
                    }
                }
            }
        });

        return tabla;
    }

    public void cargarDatos() {
        cargarPorEstado(tablaPendientes, EstadoLectura.PENDIENTE);
        cargarPorEstado(tablaLeyendo,    EstadoLectura.LEYENDO);
        cargarPorEstado(tablaLeidos,     EstadoLectura.LEIDO);
        actualizarTitulosTabs();
    }

    private void cargarPorEstado(JTable tabla,
                                  EstadoLectura estado) {
        DefaultTableModel modelo =
            (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        for (Publicacion p : biblioteca.getLibros()) {
            // Estado PERSONAL del usuario para este libro
            EstadoLectura estadoPersonal =
                usuarioActual.getEstadoLibro(p.getId());
            if (estadoPersonal == estado) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getTipo(),
                    p.getTitulo(),
                    p.getAutor(),
                    p.getGenero(),
                    p.getAñoPublicacion(),
                    obtenerTextoEstado(estadoPersonal)
                });
            }
        }
    }

    private void actualizarTitulosTabs() {
        int pendientes = ((DefaultTableModel)
            tablaPendientes.getModel()).getRowCount();
        int leyendo = ((DefaultTableModel)
            tablaLeyendo.getModel()).getRowCount();
        int leidos = ((DefaultTableModel)
            tablaLeidos.getModel()).getRowCount();

        tabbedPane.setTitleAt(0,
            "Sin Leer (" + pendientes + ")");
        tabbedPane.setTitleAt(1,
            "En Proceso (" + leyendo + ")");
        tabbedPane.setTitleAt(2,
            "Leídos (" + leidos + ")");
    }

    private void cambiarEstadoSeleccionado() {
        int tabIndex = tabbedPane.getSelectedIndex();
        JTable tabla = tabIndex == 0 ? tablaPendientes
                     : tabIndex == 1 ? tablaLeyendo
                     : tablaLeidos;

        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una publicación primero.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = (String)
            ((DefaultTableModel) tabla.getModel())
            .getValueAt(fila, 2);
        Publicacion pub = biblioteca.buscarPublicacion(titulo);
        if (pub == null) return;

        String[] opciones = {"Sin Leer", "En Proceso", "Leído"};
        EstadoLectura actual =
            usuarioActual.getEstadoLibro(pub.getId());
        int idx = actual == EstadoLectura.LEYENDO ? 1
                : actual == EstadoLectura.LEIDO   ? 2 : 0;

        String seleccion = (String) JOptionPane.showInputDialog(
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

            // Guarda en el usuario, no en la publicación
            usuarioActual.setEstadoLibro(pub.getId(), nuevo);

            GestorArchivos.guardarDatos(biblioteca,
                "data/biblioteca/biblioteca.csv",
                "data/usuarios/usuarios.csv",
                "data/biblioteca/biblioteca.json");

            cargarDatos();

            JOptionPane.showMessageDialog(this,
                "Estado actualizado.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String obtenerTextoEstado(EstadoLectura estado) {
        if (estado == null) return "Sin Leer";
        switch (estado) {
            case PENDIENTE: return "Sin Leer";
            case LEYENDO:   return "En Proceso";
            case LEIDO:     return "Leído";
            default:        return "Sin Leer";
        }
    }
}