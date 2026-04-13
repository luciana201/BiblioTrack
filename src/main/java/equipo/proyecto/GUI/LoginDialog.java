package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private Biblioteca biblioteca;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JLabel lblError;
    private JPanel panelLogin;
    private JPanel panelRegistro;
    private CardLayout cardInterno;
    private JPanel contenedor;

    public LoginDialog(JFrame parent, Biblioteca biblioteca) {
        super(parent, "BiblioTrack", true);
        this.biblioteca = biblioteca;
        setSize(360, 320);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        cardInterno = new CardLayout();
        contenedor = new JPanel(cardInterno);
        contenedor.add(crearPanelLogin(), "LOGIN");
        contenedor.add(crearPanelRegistro(), "REGISTRO");
        add(contenedor);
        cardInterno.show(contenedor, "LOGIN");
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 28, 16, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("BIBLIOTECA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(0xE87722));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(titulo, g);

        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        panel.add(new JLabel("Nombre:"), g);
        txtNombre = new JTextField(16);
        g.gridx = 1; panel.add(txtNombre, g);

        g.gridx = 0; g.gridy = 2;
        panel.add(new JLabel("Email:"), g);
        txtEmail = new JTextField(16);
        g.gridx = 1; panel.add(txtEmail, g);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(0xE87722));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        panel.add(btnEntrar, g);

        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 4; panel.add(lblError, g);

        // Enlace para ir a registro
        JLabel lblRegistro = new JLabel(
            "<html><center>¿No tienes cuenta? " +
            "<a href=''>Regístrate</a></center></html>");
        lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        lblRegistro.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardInterno.show(contenedor, "REGISTRO");
            }
        });
        g.gridy = 5; panel.add(lblRegistro, g);

        btnEntrar.addActionListener(e -> intentarLogin());
        txtEmail.addActionListener(e -> intentarLogin());

        panelLogin = panel;
        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 28, 16, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("REGISTRO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(0xE87722));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(titulo, g);

        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        panel.add(new JLabel("Nombre:"), g);
        JTextField txtNombreReg = new JTextField(16);
        g.gridx = 1; panel.add(txtNombreReg, g);

        g.gridx = 0; g.gridy = 2;
        panel.add(new JLabel("Email:"), g);
        JTextField txtEmailReg = new JTextField(16);
        g.gridx = 1; panel.add(txtEmailReg, g);

        g.gridx = 0; g.gridy = 3;
        panel.add(new JLabel("ID:"), g);
        JTextField txtIdReg = new JTextField(16);
        g.gridx = 1; panel.add(txtIdReg, g);

        JButton btnRegistrar = new JButton("Crear cuenta");
        btnRegistrar.setBackground(new Color(0xE87722));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        panel.add(btnRegistrar, g);

        JLabel lblErrorReg = new JLabel(" ");
        lblErrorReg.setForeground(Color.RED);
        lblErrorReg.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblErrorReg.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 5; panel.add(lblErrorReg, g);

        // Enlace para volver al login
        JLabel lblVolver = new JLabel(
            "<html><center>¿Ya tienes cuenta? " +
            "<a href=''>Inicia sesión</a></center></html>");
        lblVolver.setHorizontalAlignment(SwingConstants.CENTER);
        lblVolver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardInterno.show(contenedor, "LOGIN");
            }
        });
        g.gridy = 6; panel.add(lblVolver, g);

        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombreReg.getText().trim();
            String email  = txtEmailReg.getText().trim();
            String id     = txtIdReg.getText().trim();

            if (nombre.isEmpty() || email.isEmpty() || id.isEmpty()) {
                lblErrorReg.setText("Completa todos los campos.");
                return;
            }
            if (biblioteca.buscarUsuario(nombre) != null) {
                lblErrorReg.setText("Ese nombre ya existe.");
                return;
            }
            if (biblioteca.buscarUsuario(email) != null) {
                lblErrorReg.setText("Ese email ya está registrado.");
                return;
            }

            Usuario nuevo = new Usuario(nombre, id, email);
            biblioteca.agregarUsuario(nuevo);

            GestorArchivos.guardarDatos(biblioteca, AppGui.RUTA_CSV, AppGui.RUTA_USUARIOS, AppGui.RUTA_JSON);

            JOptionPane.showMessageDialog(panel,
                "Cuenta creada. Ya puedes iniciar sesión.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE);

            txtNombreReg.setText("");
            txtEmailReg.setText("");
            txtIdReg.setText("");
            cardInterno.show(contenedor, "LOGIN");
        });

        panelRegistro = panel;
        return panel;
    }

    private void intentarLogin() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            lblError.setText("Completa nombre y email.");
            return;
        }

        Usuario u = biblioteca.buscarUsuario(nombre);
        if (u == null) {
            lblError.setText("Usuario no encontrado. Regístrate.");
            return;
        }
        if (!u.getEmail().equalsIgnoreCase(email)) {
            lblError.setText("Email incorrecto.");
            return;
        }
        dispose();
        new MainFrame(biblioteca, u).setVisible(true);
    }
}