package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCuenta extends JPanel {
    
    private Biblioteca biblioteca;
    private Usuario usuarioActual;
    private MainFrame mainFrame;
    
    private JTabbedPane tabbedPane;
    private JTable tablaPendientes;
    private JTable tablaLeyendo;
    private JTable tablaLeidos;
    
    public PanelCuenta(Biblioteca biblioteca, Usuario usuario, MainFrame mainFrame) {
        this.biblioteca = biblioteca;
        this.usuarioActual = usuario;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initUI();
    }
    
    private void initUI() {
        // Panel superior con información del usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelUsuario.setBorder(BorderFactory.createTitledBorder("Mi Cuenta"));
        
        JLabel lblNombre = new JLabel("Usuario: " + usuarioActual.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelUsuario.add(lblNombre);
        
        JLabel lblEmail = new JLabel("Email: " + usuarioActual.getEmail());
        panelUsuario.add(lblEmail);
        
        JLabel lblId = new JLabel("ID: " + usuarioActual.getId());
        lblId.setForeground(Color.GRAY);
        panelUsuario.add(lblId);
        
        add(panelUsuario, BorderLayout.NORTH);
        
        // Panel con tabs para los estados
        tabbedPane = new JTabbedPane();
        
        // Tab Pendientes
        tablaPendientes = crearTabla();
        JScrollPane scrollPendientes = new JScrollPane(tablaPendientes);
        tabbedPane.addTab("Sin Leer", scrollPendientes);
        
        // Tab Leyendo
        tablaLeyendo = crearTabla();
        JScrollPane scrollLeyendo = new JScrollPane(tablaLeyendo);
        tabbedPane.addTab("En Proceso", scrollLeyendo);
        
        // Tab Leídos
        tablaLeidos = crearTabla();
        JScrollPane scrollLeidos = new JScrollPane(tablaLeidos);
        tabbedPane.addTab("Leídos", scrollLeidos);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnCambiarEstado = new JButton("Cambiar Estado de Lectura");
        btnCambiarEstado.setBackground(new Color(0xE87722));
        btnCambiarEstado.setForeground(Color.WHITE);
        btnCambiarEstado.setFocusPainted(false);
        btnCambiarEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        panelBotones.add(btnCambiarEstado);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarDatos());
        panelBotones.add(btnActualizar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Cargar datos iniciales
        cargarDatos();
    }
    
    private JTable crearTabla() {
        String[] columnas = {"ID", "Tipo", "Título", "Autor", "Género", "Año", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Doble click para ver detalles
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int fila = tabla.getSelectedRow();
                    if (fila >= 0) {
                        String titulo = (String) modelo.getValueAt(fila, 2);
                        Publicacion pub = biblioteca.buscarPublicacion(titulo);
                        if (pub != null) {
                            mainFrame.abrirDetalle(pub);
                        }
                    }
                }
            }
        });
        
        return tabla;
    }
    
    public void cargarDatos() {
        cargarPublicacionesPorEstado(tablaPendientes, EstadoLectura.PENDIENTE);
        cargarPublicacionesPorEstado(tablaLeyendo, EstadoLectura.LEYENDO);
        cargarPublicacionesPorEstado(tablaLeidos, EstadoLectura.LEIDO);
        
        // Actualizar títulos con contadores
        actualizarTitulosTabs();
    }
    
    private void cargarPublicacionesPorEstado(JTable tabla, EstadoLectura estado) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        List<Publicacion> publicaciones = biblioteca.getLibrosEstado(estado);
        
        for (Publicacion p : publicaciones) {
            String estadoTexto = obtenerTextoEstado(p.getEstado());
            
            modelo.addRow(new Object[]{
                p.getId(),
                p.getTipo(),
                p.getTitulo(),
                p.getAutor(),
                p.getGenero(),
                p.getAñoPublicacion(),
                estadoTexto
            });
        }
    }
    
    private void actualizarTitulosTabs() {
        int pendientes = ((DefaultTableModel) tablaPendientes.getModel()).getRowCount();
        int leyendo = ((DefaultTableModel) tablaLeyendo.getModel()).getRowCount();
        int leidos = ((DefaultTableModel) tablaLeidos.getModel()).getRowCount();
        
        tabbedPane.setTitleAt(0, "Pendiente (" + pendientes + ")");
        tabbedPane.setTitleAt(1, "Leyendo (" + leyendo + ")");
        tabbedPane.setTitleAt(2, "Leídos (" + leidos + ")");
    }
    
    private void cambiarEstadoSeleccionado() {
        int tabIndex = tabbedPane.getSelectedIndex();
        JTable tabla = null;
        
        if (tabIndex == 0) tabla = tablaPendientes;
        else if (tabIndex == 1) tabla = tablaLeyendo;
        else if (tabIndex == 2) tabla = tablaLeidos;
        
        if (tabla == null) return;
        
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una publicación",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String titulo = (String) ((DefaultTableModel) tabla.getModel()).getValueAt(fila, 2);
        Publicacion pub = biblioteca.buscarPublicacion(titulo);
        
        if (pub == null) return;
        
        // Mostrar diálogo para cambiar estado
        String[] opciones = {"Pendiente","Leyendo", "Leído"};
        int estadoActual = pub.getEstado().ordinal();
        
        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Seleccione el nuevo estado de lectura:",
            "Cambiar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[estadoActual]
        );
        
        if (seleccion != null) {
            EstadoLectura nuevoEstado = null;
            if ("Pendiente".equals(seleccion)) nuevoEstado = EstadoLectura.PENDIENTE;
            else if ("Leyendo".equals(seleccion)) nuevoEstado = EstadoLectura.LEYENDO;
            else if ("Leído".equals(seleccion)) nuevoEstado = EstadoLectura.LEIDO;
            
            if (nuevoEstado != null) {
                pub.setEstado(nuevoEstado);
                
                // Guardar cambios
                GestorArchivos.guardarDatos(biblioteca,
                    "data/biblioteca/biblioteca.csv",
                    "data/usuarios/usuarios.csv",
                    "data/biblioteca/biblioteca.json");
                
                cargarDatos();
                
                JOptionPane.showMessageDialog(this,
                    "Estado actualizado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private String obtenerTextoEstado(EstadoLectura estado) {
        if (estado == null) return "-";
        
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case LEYENDO: return "Leyendo";
            case LEIDO: return "Leído";
            default: return "Pendiente";
        }
    }
}
