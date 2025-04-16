import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class SignUpForm extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, usernameField;
    private JPasswordField passwordField;

    public SignUpForm() {
        setTitle("Créer un compte ✍");
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(60, 63, 65));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Panel prénom
        JPanel firstNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstNamePanel.setBackground(new Color(60, 63, 65));
        JLabel firstNameLabel = new JLabel("👤 Prénom :");
        firstNameLabel.setForeground(Color.WHITE);
        firstNamePanel.add(firstNameLabel);

        firstNameField = new JTextField(20);
        firstNameField.setBackground(Color.WHITE);
        firstNameField.setForeground(Color.BLACK);
        firstNameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        firstNameField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        firstNameField.setMargin(new Insets(10, 10, 10, 10));
        firstNamePanel.add(firstNameField);
        contentPanel.add(firstNamePanel);

        // Panel nom
        JPanel lastNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lastNamePanel.setBackground(new Color(60, 63, 65));
        JLabel lastNameLabel = new JLabel("👤 Nom :");
        lastNameLabel.setForeground(Color.WHITE);
        lastNamePanel.add(lastNameLabel);

        lastNameField = new JTextField(20);
        lastNameField.setBackground(Color.WHITE);
        lastNameField.setForeground(Color.BLACK);
        lastNameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lastNameField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        lastNameField.setMargin(new Insets(10, 10, 10, 10));
        lastNamePanel.add(lastNameField);
        contentPanel.add(lastNamePanel);

        // Panel email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setBackground(new Color(60, 63, 65));
        JLabel emailLabel = new JLabel("📧 Email :");
        emailLabel.setForeground(Color.WHITE);
        emailPanel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.BLACK);
        emailField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        emailField.setMargin(new Insets(10, 10, 10, 10));
        emailPanel.add(emailField);
        contentPanel.add(emailPanel);

        // Panel téléphone
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.setBackground(new Color(60, 63, 65));
        JLabel phoneLabel = new JLabel("📱 Numéro de téléphone :");
        phoneLabel.setForeground(Color.WHITE);
        phonePanel.add(phoneLabel);

        phoneField = new JTextField(20);
        phoneField.setBackground(Color.WHITE);
        phoneField.setForeground(Color.BLACK);
        phoneField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        phoneField.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        phoneField.setMargin(new Insets(10, 10, 10, 10));
        phonePanel.add(phoneField);
        contentPanel.add(phonePanel);

        // Panel nom d'utilisateur
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(new Color(60, 63, 65));
        JLabel usernameLabel = new JLabel("👤 Nom d'utilisateur :");
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

        // Panel mot de passe
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(new Color(60, 63, 65));
        JLabel passwordLabel = new JLabel("🔒 Mot de passe :");
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

        // Panel bouton
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(60, 63, 65));

        JButton signUpButton = new JButton("✅ Créer le compte");
        setButtonStyle(signUpButton, new Color(34, 139, 34));
        signUpButton.addActionListener(e -> createAccount());
        buttonPanel.add(signUpButton);

        contentPanel.add(buttonPanel);

        getContentPane().setBackground(new Color(60, 63, 65));
        add(contentPanel, BorderLayout.CENTER);

        setSize(500, 650);  // Ajuster la taille pour inclure tous les champs
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setButtonStyle(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        btn.setFocusPainted(false);
    }

    private void createAccount() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validation du numéro de téléphone (s'il est vide ou invalide)
        if (phone.isEmpty() || !phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Numéro de téléphone invalide. Il doit contenir 10 chiffres.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            // Enregistrement dans le tableau user
            String query = "INSERT INTO users (username, password, role, email, telephone, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "client");
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, LocalDateTime.now().toString());
            stmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            // Enregistrement dans le tableau client
            if (userId != -1) {
                String clientQuery = "INSERT INTO client (id_client, nom, prenom) VALUES (?, ?, ?)";
                PreparedStatement clientStmt = conn.prepareStatement(clientQuery);
                clientStmt.setInt(1, userId);
                clientStmt.setString(2, firstName);
                clientStmt.setString(3, lastName);
                clientStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Compte créé avec succès 🎉 !");
            dispose(); // Ferme la fenêtre actuelle
            new ClientPage(username, userId).setVisible(true); // Redirige vers l’espace client avec le username et userId
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpForm().setVisible(true));
    }
}
