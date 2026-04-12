package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelInicio extends JPanel {
    
    private Biblioteca biblioteca;
    private Usuario usuarioActual;
    private MainFrame mainFrame;
    
    private static final String[] GENEROS = {
        "Thriller", "Romance", "Ficción", "Ciencia/Letras", 
        "Terror", "Clásicos", "Comic", "Manga"
    };
    
    public PanelInicio(Biblioteca biblioteca, Usuario usuario, MainFrame mainFrame) {
        this.biblioteca = biblioteca;
        this.usuarioActual = usuario;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }
    
    private void initUI() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // RECOMENDACIONES SEMANALES
        JPanel recomendacionesPanel = crearRecomendaciones();
        contentPanel.add(recomendacionesPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // NOVELAS
        JPanel novelasPanel = crearSeccionGenero("NOVELAS", "Novela");
        contentPanel.add(novelasPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // COMICS
        JPanel comicsPanel = crearSeccionGenero("COMICS", "Comic");
        contentPanel.add(comicsPanel);
        
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }
    
    private JPanel crearRecomendaciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel titulo = new JLabel("RECOMENDACIONES SEMANALES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(new Color(0xE87722));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        // Obtener las 6 publicaciones mejor calificadas
        List<Publicacion> recomendadas = obtenerMejorCalificadas(6);
        
        for (Publicacion pub : recomendadas) {
            JPanel card = crearTarjetaLibro(pub);
            cardsPanel.add(card);
        }
        
        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearSeccionGenero(String tituloSeccion, String tipoPublicacion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel titulo = new JLabel(tituloSeccion);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(new Color(0xE87722));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        // Filtrar por tipo de publicación
        List<Publicacion> publicaciones = biblioteca.getLibros().stream()
            .filter(p -> p.getClass().getSimpleName().equals(tipoPublicacion))
            .limit(6)
            .collect(java.util.stream.Collectors.toList());
        
        for (Publicacion pub : publicaciones) {
            JPanel card = crearTarjetaLibro(pub);
            cardsPanel.add(card);
        }
        
        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearTarjetaLibro(Publicacion pub) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(140, 200));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Portada
        JPanel portada = new JPanel();
        portada.setPreferredSize(new Dimension(140, 160));
        portada.setBackground(new Color(0xE87722));
        portada.setLayout(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("<html><center>" + pub.getTitulo() + "</center></html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        portada.add(lblTitulo, BorderLayout.CENTER);
        
        card.add(portada, BorderLayout.CENTER);
        
        // Evento click
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainFrame.abrirDetalle(pub);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createLineBorder(new Color(0xE87722), 2));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });
        
        return card;
    }
    
    private List<Publicacion> obtenerMejorCalificadas(int limite) {
        List<Publicacion> todas = new ArrayList<>(biblioteca.getLibros());
        
        todas.sort((a, b) -> {
            double mediaA = calcularMediaDe(a);
            double mediaB = calcularMediaDe(b);
            return Double.compare(mediaB, mediaA);
        });
        
        return todas.stream().limit(limite).collect(java.util.stream.Collectors.toList());
    }
    
    private double calcularMediaDe(Publicacion pub) {
        return biblioteca.getUsuarios().stream()
            .flatMap(u -> u.getReseñas().stream())
            .filter(r -> r.getPublicacion().getTitulo().equals(pub.getTitulo()))
            .mapToInt(Reseña::getCalificacion)
            .average().orElse(0);
    }
}
