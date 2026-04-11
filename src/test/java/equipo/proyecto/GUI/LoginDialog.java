package equipo.proyecto.GUI;

import equipo.proyecto.*;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

   @SuppressWarnings("unused")
    private Usuario usuarioAutenticado = null;
    private Biblioteca biblioteca;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JLabel lblError;

    public LoginDialog(JFrame parent, Biblioteca biblioteca) {
        super(parent, "BiblioTrack — Iniciar sesión", true);
        this.biblioteca = biblioteca;
        setSize(340, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initUI();
    }

    private void initUI() {
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

        add(panel, BorderLayout.CENTER);
        btnEntrar.addActionListener(e -> intentarLogin());
        txtEmail.addActionListener(e -> intentarLogin());
    }
private void intentarLogin() {
    String nombre = txtNombre.getText().trim();
    String email  = txtEmail.getText().trim();

    if (nombre.isEmpty() || email.isEmpty()) {
        lblError.setText("Completa nombre y email.");
        return;
    }

    // Si el usuario no existe, lo crea automáticamente
    Usuario u = biblioteca.buscarUsuario(nombre);
    if (u == null) {
        u = new Usuario(nombre, nombre.toLowerCase(), email);
        biblioteca.agregarUsuario(u);
    }

    usuarioAutenticado = u;
    dispose();
    new MainFrame(biblioteca, u).setVisible(true);
}

}