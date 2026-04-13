package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;

public class DialogoAgregarPublicacion extends JDialog {
    
    private Biblioteca biblioteca;
    private boolean guardado = false;
    
    private JComboBox<String> comboTipo;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtISBN;
    private JSpinner spinnerAnio;
    private JComboBox<String> comboGenero;
    
    // Campos específicos Novela
    private JSpinner spinnerPaginas;
    
    // Campos específicos Comic
    private JSpinner spinnerVolumen;
    private JTextField txtDemografia;
    
    // Campos específicos Libro Técnico
    private JTextField txtTema;
    private JComboBox<String> comboNivel;
    
    private JPanel panelCamposEspecificos;
    
    private static final String[] TIPOS = {"Novela", "Comic", "Libro Técnico"};
    private static final String[] GENEROS = {
        "Thriller", "Romance", "Ficción", "Ciencia/Letras", 
        "Terror", "Clásicos", "Comic", "Manga"
    };
    private static final String[] NIVELES = {"Básico", "Intermedio", "Avanzado", "Experto"};
    
    public DialogoAgregarPublicacion(JFrame parent, Biblioteca biblioteca) {
        super(parent, "Agregar Nueva Publicación", true);
        this.biblioteca = biblioteca;
        
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initUI();
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de campos comunes
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("Tipo:*"), gbc);
        gbc.gridx = 1;
        comboTipo = new JComboBox<>(TIPOS);
        comboTipo.addActionListener(e -> actualizarCamposEspecificos());
        camposPanel.add(comboTipo, gbc);
        
        row++;
        
        // Título
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("Título:*"), gbc);
        gbc.gridx = 1;
        txtTitulo = new JTextField(20);
        camposPanel.add(txtTitulo, gbc);
        
        row++;
        
        // Autor
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("Autor:*"), gbc);
        gbc.gridx = 1;
        txtAutor = new JTextField(20);
        camposPanel.add(txtAutor, gbc);
        
        row++;
        
        // ISBN
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("ISBN:*"), gbc);
        gbc.gridx = 1;
        txtISBN = new JTextField(20);
        camposPanel.add(txtISBN, gbc);
        
        row++;
        
        // Año
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("Año:*"), gbc);
        gbc.gridx = 1;
        spinnerAnio = new JSpinner(new SpinnerNumberModel(2024, 1800, 2099, 1));
        camposPanel.add(spinnerAnio, gbc);
        
        row++;
        
        // Género
        gbc.gridx = 0; gbc.gridy = row;
        camposPanel.add(new JLabel("Género:*"), gbc);
        gbc.gridx = 1;
        comboGenero = new JComboBox<>(GENEROS);
        camposPanel.add(comboGenero, gbc);
        
        row++;
        
        mainPanel.add(camposPanel, BorderLayout.NORTH);
        
        // Panel para campos específicos
        panelCamposEspecificos = new JPanel(new GridBagLayout());
        panelCamposEspecificos.setBorder(BorderFactory.createTitledBorder("Campos Específicos"));
        
        // Inicializar campos específicos
        inicializarCamposEspecificos();
        actualizarCamposEspecificos();
        
        mainPanel.add(panelCamposEspecificos, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0xE87722));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarPublicacion());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void inicializarCamposEspecificos() {
        // Campos Novela
        spinnerPaginas = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        
        // Campos Comic
        spinnerVolumen = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        txtDemografia = new JTextField(15);
        
        // Campos Libro Técnico
        txtTema = new JTextField(15);
        comboNivel = new JComboBox<>(NIVELES);
    }
    
    private void actualizarCamposEspecificos() {
        panelCamposEspecificos.removeAll();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        
        if ("Novela".equals(tipoSeleccionado)) {
            gbc.gridx = 0; gbc.gridy = 0;
            panelCamposEspecificos.add(new JLabel("Número de Páginas:*"), gbc);
            gbc.gridx = 1;
            panelCamposEspecificos.add(spinnerPaginas, gbc);
            
        } else if ("Comic".equals(tipoSeleccionado)) {
            gbc.gridx = 0; gbc.gridy = 0;
            panelCamposEspecificos.add(new JLabel("Volumen:*"), gbc);
            gbc.gridx = 1;
            panelCamposEspecificos.add(spinnerVolumen, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panelCamposEspecificos.add(new JLabel("Demografía:*"), gbc);
            gbc.gridx = 1;
            panelCamposEspecificos.add(txtDemografia, gbc);
            
        } else if ("Libro Técnico".equals(tipoSeleccionado)) {
            gbc.gridx = 0; gbc.gridy = 0;
            panelCamposEspecificos.add(new JLabel("Tema:*"), gbc);
            gbc.gridx = 1;
            panelCamposEspecificos.add(txtTema, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panelCamposEspecificos.add(new JLabel("Nivel:*"), gbc);
            gbc.gridx = 1;
            panelCamposEspecificos.add(comboNivel, gbc);
        }
        
        panelCamposEspecificos.revalidate();
        panelCamposEspecificos.repaint();
    }
    
    private void guardarPublicacion() {
        // Validar campos comunes
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtISBN.getText().trim();
        int año = (int) spinnerAnio.getValue();
        String genero = (String) comboGenero.getSelectedItem();
        String tipo = (String) comboTipo.getSelectedItem();
        
        if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos obligatorios (*)",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Publicacion publicacion = null;
        
        try {
            if ("Novela".equals(tipo)) {
                int paginas = (int) spinnerPaginas.getValue();
                publicacion = new Novela(titulo, tipo, autor, año, isbn, genero, paginas);
                
            } else if ("Comic".equals(tipo)) {
                int volumen = (int) spinnerVolumen.getValue();
                String demografia = txtDemografia.getText().trim();
                
                if (demografia.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor complete el campo Demografía",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                publicacion = new Comic(titulo, tipo, autor, año, isbn, genero, volumen, demografia);
                
            } else if ("Libro Técnico".equals(tipo)) {
                String tema = txtTema.getText().trim();
                String nivel = (String) comboNivel.getSelectedItem();
                
                if (tema.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor complete el campo Tema",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                publicacion = new LibroTecnico(titulo, tipo, autor, año, isbn, genero, tema, nivel);
            }
            
            if (publicacion != null) {
                // Estado por defecto vacío (PENDIENTE con visualización especial)
                publicacion.setEstado(EstadoLectura.PENDIENTE);
                biblioteca.agregarPublicacion(publicacion);
                
                // Guardar cambios
                GestorArchivos.guardarDatos(biblioteca, 
                    "data/biblioteca/biblioteca.csv",
                    "data/usuarios/usuarios.csv", 
                    "data/biblioteca/biblioteca.json");
                
                guardado = true;
                JOptionPane.showMessageDialog(this, 
                    "Publicación agregada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar la publicación: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isGuardado() {
        return guardado;
    }
}
