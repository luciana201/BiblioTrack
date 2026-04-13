package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelInicio extends JPanel {

    private Biblioteca biblioteca;
    private Usuario UsuarioActual;
    private MainFrame mainFrame;

    private static final String[] COLORES_PORTADA = {
        "#E87722", "#185FA5", "#0F6E56", "#993C1D",
        "#534AB7", "#BA7517", "#A32D2D", "#3B6D11"
    };

    public PanelInicio(Biblioteca biblioteca, Usuario usuario, MainFrame mainFrame) {
        this.biblioteca = biblioteca;
        this.UsuarioActual = usuario;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(crearSeccion("RECOMENDACIONES SEMANALES", biblioteca.getLibroMejorCalificados(6)));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(crearSeccion("NOVELAS", biblioteca.filtrarPorTipo("Novela", 10)));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(crearSeccion("COMICS", biblioteca.filtrarPorTipo("Comic", 10)));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(crearSeccion("LIBROS TÉCNICOS", biblioteca.filtrarPorTipo("Libro Técnico", 15)));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearSeccion(String titulo, List<Publicacion> libros) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("  " + titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(0xE87722));
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE87722), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        cardsPanel.setOpaque(false);

        if (libros.isEmpty()) {
            JLabel vacio = new JLabel("  No hay publicaciones aún.");
            vacio.setForeground(Color.GRAY);
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 12));
            cardsPanel.add(vacio);
        } else {
            int colorIdx = 0;
            for (Publicacion pub : libros) {
                cardsPanel.add(crearTarjeta(pub, COLORES_PORTADA[colorIdx % COLORES_PORTADA.length]));
                colorIdx++;
            }
        }

        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTarjeta(Publicacion pub, String colorHex) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(130, 195));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel portada = new JPanel(new BorderLayout());
        portada.setPreferredSize(new Dimension(130, 155));
        Color color = Color.decode(colorHex);
        portada.setBackground(color);

        ImageIcon icono = cargarImagen(pub);
        if (icono != null) {
            JLabel imgLabel = new JLabel(icono);
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            portada.add(imgLabel, BorderLayout.CENTER);
        } else {
            JLabel lblAutor = new JLabel("<html><center><i>"
                + pub.getAutor() + "</i></center></html>");
            lblAutor.setForeground(new Color(255, 255, 255, 180));
            lblAutor.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lblAutor.setHorizontalAlignment(SwingConstants.CENTER);
            lblAutor.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
            portada.add(lblAutor, BorderLayout.CENTER);
        }

        card.add(portada, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel(
            "<html><center>" + pub.getTitulo() + "</center></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        lblTitulo.setPreferredSize(new Dimension(130, 38));
        card.add(lblTitulo, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.abrirDetalle(pub);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(
                    new Color(0xE87722), 2));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(
                    Color.LIGHT_GRAY, 1));
            }
        });

        return card;
    }

    private ImageIcon cargarImagen(Publicacion pub) {
        try {
            String ruta = "/imagenes/" + pub.getId() + ".jpg";
            java.net.URL url = getClass().getResource(ruta);
            if (url == null) {
                String nombreArchivo = pub.getTitulo()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]", "_") + ".jpg";
                url = getClass().getResource("/imagenes/" + nombreArchivo);
            }
            if (url != null) {
                ImageIcon original = new ImageIcon(url);
                Image scaled = original.getImage()
                    .getScaledInstance(130, 155, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception ignored) {}
        return null;
    }

}