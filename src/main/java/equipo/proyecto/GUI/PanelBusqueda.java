package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelBusqueda extends JPanel {
    
    private Biblioteca biblioteca;
    private MainFrame mainFrame;
    private JTextField txtBusqueda;
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboTipo;
    private JTable tablaPubs;
    private DefaultTableModel modeloTabla;
    
    private static final String[] GENEROS = {
        "Todos", "Thriller", "Romance", "Ficción", "Ciencia/Letras", 
        "Terror", "Clásicos", "Comic", "Manga"
    };
    
    private static final String[] TIPOS = {
        "Todos", "Novela", "Comic", "Libro Técnico"
    };
    
    public PanelBusqueda(Biblioteca biblioteca, MainFrame mainFrame) {
        this.biblioteca = biblioteca;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }
    
    private void initUI() {
        // Panel superior con filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Búsqueda y Filtros"));
        
        panelFiltros.add(new JLabel("Búsqueda:"));
        txtBusqueda = new JTextField(20);
        panelFiltros.add(txtBusqueda);
        
        panelFiltros.add(new JLabel("Género:"));
        comboGenero = new JComboBox<>(GENEROS);
        panelFiltros.add(comboGenero);
        
        panelFiltros.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(TIPOS);
        panelFiltros.add(comboTipo);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0xE87722));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> realizarBusqueda());
        panelFiltros.add(btnBuscar);
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        panelFiltros.add(btnLimpiar);
        
        add(panelFiltros, BorderLayout.NORTH);
        
        // Tabla de resultados
        String[] columnas = {"ID", "Título", "Autor", "Género", "Tipo", "Año"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };  
        
        tablaPubs = new JTable(modeloTabla);
        tablaPubs.setRowHeight(28);
        tablaPubs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPubs.getTableHeader().setReorderingAllowed(false);
        
        // Doble click para ver detalles
        tablaPubs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int fila = tablaPubs.getSelectedRow();
                    if (fila >= 0) {
                        String titulo = (String) modeloTabla.getValueAt(fila, 1);
                        Publicacion pub = biblioteca.buscarPublicacion(titulo);
                        if (pub != null) {
                            mainFrame.abrirDetalle(pub);
                        }
                    }
                }
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaPubs);
        add(scrollTabla, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnAgregar = new JButton("Agregar Publicación");
        btnAgregar.setBackground(new Color(0xE87722));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> abrirDialogoAgregar());
        panelBotones.add(btnAgregar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Cargar todas las publicaciones al inicio
        cargarTodasPublicaciones();
    }
    
    private void realizarBusqueda() {
        String textoBusqueda = txtBusqueda.getText().trim();
        String generoSeleccionado = (String) comboGenero.getSelectedItem();
        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        
        List<Publicacion> resultados = new ArrayList<>(biblioteca.getLibros());
        
        // Filtrar por texto de búsqueda
        if (!textoBusqueda.isEmpty()) {
            resultados = resultados.stream()
                .filter(p -> p.getTitulo().toLowerCase().contains(textoBusqueda.toLowerCase()) ||
                            p.getAutor().toLowerCase().contains(textoBusqueda.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Filtrar por género
        if (!"Todos".equals(generoSeleccionado)) {
            resultados = resultados.stream()
                .filter(p -> generoSeleccionado.equalsIgnoreCase(p.getGenero()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Filtrar por tipo
        if (!"Todos".equals(tipoSeleccionado)) {
            resultados = resultados.stream()
                .filter(p -> p.getTipo().equals(tipoSeleccionado))
                .collect(java.util.stream.Collectors.toList());
        }
        
        actualizarTabla(resultados);
    }
    
    private void cargarTodasPublicaciones() {
        actualizarTabla(biblioteca.getLibros());
    }
    
    private void actualizarTabla(List<Publicacion> publicaciones) {
        modeloTabla.setRowCount(0);
        
        for (Publicacion p : publicaciones) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getTitulo(),
                p.getAutor(),
                p.getGenero(),
                p.getTipo(),
                p.getAñoPublicacion()
            });
        }
    }
    
    private void limpiarFiltros() {
        txtBusqueda.setText("");
        comboGenero.setSelectedIndex(0);
        comboTipo.setSelectedIndex(0);
        cargarTodasPublicaciones();
    }
    
    private void abrirDialogoAgregar() {
        DialogoAgregarPublicacion dialogo = new DialogoAgregarPublicacion(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            biblioteca
        );
        dialogo.setVisible(true);
        
        if (dialogo.isGuardado()) {
            realizarBusqueda(); // Actualizar resultados
        }
    }
}
