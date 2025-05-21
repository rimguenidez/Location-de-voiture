import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Connexion");
        setLayout(new BorderLayout());

        Color backgroundColor = new Color(60, 63, 65); // gris clair

        // Panel principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(backgroundColor);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Champ nom d'utilisateur
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setBackground(backgroundColor);
        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        usernameLabel.setForeground(Color.WHITE);
        usernamePanel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.BLACK);
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        usernameField.setMargin(new Insets(10, 10, 10, 10));
        usernamePanel.add(usernameField);
        contentPanel.add(usernamePanel);

        // Champ mot de passe
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setBackground(backgroundColor);
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setForeground(Color.WHITE);
        passwordPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        passwordField.setMargin(new Insets(10, 10, 10, 10));
        passwordPanel.add(passwordField);
        contentPanel.add(passwordPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Les boutons 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(backgroundColor);

        JButton loginButton = new JButton("🔑 Se connecter");
        setButtonStyle(loginButton, new Color(34, 139, 34));
        loginButton.addActionListener(e -> checkLogin());
        buttonPanel.add(loginButton);

        JButton signUpButton = new JButton("✍ Créer un compte");
        setButtonStyle(signUpButton, new Color(255, 69, 0), new Dimension(180, 40));
        signUpButton.addActionListener(e -> new SignUpForm().setVisible(true));
        buttonPanel.add(signUpButton);

        contentPanel.add(buttonPanel);

        getContentPane().setBackground(backgroundColor);
        add(contentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        footerPanel.setBackground(backgroundColor);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 120));

        footerPanel.add(createFooterItem("🚗", "Voitures neuves"));
        footerPanel.add(createFooterItem("⏰", "Service 24h/24"));
        footerPanel.add(createFooterItem("💰", "Tarifs abordables"));

        add(footerPanel, BorderLayout.SOUTH);

        // Fenêtre
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JPanel createFooterItem(String emoji, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel emojiLabel = new JLabel(emoji, SwingConstants.CENTER);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        emojiLabel.setForeground(Color.WHITE);

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        textLabel.setForeground(Color.WHITE);
        textLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        panel.add(emojiLabel);
        panel.add(textLabel);

        return panel;
    }

    private void setButtonStyle(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        btn.setFocusPainted(false);
    }

    private void setButtonStyle(JButton btn, Color color, Dimension size) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(size);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        btn.setFocusPainted(false);
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            String query = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");

                if ("client".equals(role)) {
                    CardLayout layout = new CardLayout();
                    JPanel container = new JPanel(layout);
                    ClientPage panel = new ClientPage(username, id);
                    container.add(panel, "GererLocations");
                    layout.show(container, "GererLocations");

                    JFrame frame = new JFrame("Gérer Locations 🚗");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(800, 600);
                    frame.setLocationRelativeTo(null);
                    frame.setContentPane(container);
                    frame.setVisible(true);
                } else if ("admin".equals(role)) {
                    new AdminPage(username).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
