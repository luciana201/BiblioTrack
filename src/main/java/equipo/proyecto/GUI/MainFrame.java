package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Biblioteca biblioteca;
    private Usuario usuarioActual;
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JLabel labelEstado;

    // Paneles
    private PanelInicio panelInicio;
    private PanelBusqueda panelBusqueda;
    private PanelCuenta panelCuenta;

    public MainFrame(Biblioteca biblioteca, Usuario usuario) {
        this.biblioteca = biblioteca;
        this.usuarioActual = usuario;
        initUI();
    }

    private void initUI() {
        setTitle("BiblioTrack");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra superior
        add(crearTopBar(), BorderLayout.NORTH);

        // Contenido central con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Crear paneles
        panelInicio = new PanelInicio(biblioteca, usuarioActual, this);
        panelBusqueda = new PanelBusqueda(biblioteca, this);
        panelCuenta = new PanelCuenta(biblioteca, usuarioActual, this);

        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelBusqueda, "BUSCAR");
        panelContenido.add(panelCuenta, "CUENTA");

        add(panelContenido, BorderLayout.CENTER);

        // Barra de estado inferior
        labelEstado = new JLabel(
                "  Sesión: " + usuarioActual.getNombre()
                        + " | " + usuarioActual.getEmail());
        labelEstado.setBorder(
                BorderFactory.createEtchedBorder());
        add(labelEstado, BorderLayout.SOUTH);
    }

    private JPanel crearTopBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, Color.LIGHT_GRAY));

        // Logo
        JLabel logo = new JLabel("BIBLIOTECA");
        logo.setFont(new Font("SansSerif", Font.BOLD, 18));
        logo.setForeground(new Color(0xE87722));
        bar.add(logo, BorderLayout.WEST);

        // Botones del centro: INICIO y BUSCAR
        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 6, 0));
        btnPanel.setOpaque(false);

        JButton btnInicio = new JButton("INICIO");
        JButton btnBuscar = new JButton("BÚSQUEDA");

        btnInicio.setFocusPainted(false);
        btnBuscar.setFocusPainted(false);

        btnInicio.addActionListener(e -> {
            panelContenido.remove(panelInicio);
            panelInicio = new PanelInicio(biblioteca, usuarioActual, this);
            panelContenido.add(panelInicio, "INICIO");
            cardLayout.show(panelContenido, "INICIO");
        });

        btnBuscar.addActionListener(e -> {
            cardLayout.show(panelContenido, "BUSCAR");
        });

        btnPanel.add(btnInicio);
        btnPanel.add(btnBuscar);
        bar.add(btnPanel, BorderLayout.CENTER);

        // Menú "Tu cuenta" a la derecha
        JButton btnCuenta = new JButton("TU CUENTA");
        btnCuenta.setFocusPainted(false);

        JPopupMenu menuCuenta = new JPopupMenu();
        JMenuItem itemVerCuenta = new JMenuItem("Ver cuenta");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");

        itemVerCuenta.addActionListener(e -> {
            panelCuenta.cargarDatos(); // Actualizar datos antes de mostrar
            cardLayout.show(panelContenido, "CUENTA");
        });

        itemCerrarSesion.addActionListener(e -> {
            // Guardar datos antes de cerrar sesión
            GestorArchivos.guardarDatos(biblioteca,
                    "data/biblioteca/biblioteca.csv",
                    "data/usuarios/usuarios.csv",
                    "data/biblioteca/biblioteca.json");

            dispose();
            // Volver al login
            new LoginDialog(null, biblioteca)
                    .setVisible(true);
        });

        menuCuenta.add(itemVerCuenta);
        menuCuenta.add(itemCerrarSesion);

        btnCuenta.addActionListener(e -> menuCuenta.show(btnCuenta, 0,
                btnCuenta.getHeight()));

        bar.add(btnCuenta, BorderLayout.EAST);
        return bar;
    }

    // Lo usará PanelDetalleLibro para abrirse
    public void abrirDetalle(Publicacion pub) {
        PanelDetalleLibro detalle = new PanelDetalleLibro(
                pub, usuarioActual,
                biblioteca, labelEstado, this);
        panelContenido.add(detalle, "DETALLE");
        cardLayout.show(panelContenido, "DETALLE");
    }

    public void volverInicio() {
        panelContenido.remove(panelInicio);
        panelInicio = new PanelInicio(biblioteca, usuarioActual, this);
        panelContenido.add(panelInicio, "INICIO");
        cardLayout.show(panelContenido, "INICIO");
    }
}
