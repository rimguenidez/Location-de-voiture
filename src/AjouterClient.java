import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AjouterClient extends JFrame {

    public AjouterClient() {
        setTitle("Ajouter un Client ➕");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Ajouter un Client 📝");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nomLabel = new JLabel("Nom 🏷️ :");
        JLabel prenomLabel = new JLabel("Prénom 🏷️ :");
        JLabel emailLabel = new JLabel("Email 📧 :");
        JLabel phoneLabel = new JLabel("Téléphone 📱 :");
        JLabel usernameLabel = new JLabel("Nom d'utilisateur 🆔 :");
        JLabel passwordLabel = new JLabel("Mot de passe 🔒 :");

        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JLabel[] labels = {nomLabel, prenomLabel, emailLabel, phoneLabel, usernameLabel, passwordLabel};
        JTextField[] fields = {nomField, prenomField, emailField, phoneField, usernameField, passwordField};

        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            label.setForeground(Color.WHITE);
        }

        for (JTextField field : fields) {
            field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            field.setBackground(Color.WHITE);
        }

        String[] fieldLabels = {"Nom", "Prénom", "Email", "Téléphone", "Nom d'utilisateur", "Mot de passe"};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        JButton ajouterBtn = new JButton("Ajouter ➕");
        JButton retourBtn = new JButton("Retour 🔙");

        ajouterBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        retourBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));

        ajouterBtn.setBackground(new Color(0, 123, 255));
        retourBtn.setBackground(new Color(34, 177, 76));
        ajouterBtn.setForeground(Color.WHITE);
        retourBtn.setForeground(Color.WHITE);

        ajouterBtn.setFocusPainted(false);
        retourBtn.setFocusPainted(false);
        ajouterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        retourBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ajouterBtn.setMaximumSize(new Dimension(300, 40));
        retourBtn.setMaximumSize(new Dimension(300, 40));
        ajouterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        ajouterBtn.addActionListener((ActionEvent e) -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
                String userQuery = "INSERT INTO users (role, email, telephone, username, password) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                    userStmt.setString(1, "client");
                    userStmt.setString(2, email);
                    userStmt.setString(3, phone);
                    userStmt.setString(4, username);
                    userStmt.setString(5, password);
                    userStmt.executeUpdate();

                    try (ResultSet rs = userStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int userId = rs.getInt(1);

                            String clientQuery = "INSERT INTO client (id_client, nom, prenom) VALUES (?, ?, ?)";
                            try (PreparedStatement clientStmt = conn.prepareStatement(clientQuery)) {
                                clientStmt.setInt(1, userId);
                                clientStmt.setString(2, nom);
                                clientStmt.setString(3, prenom);
                                clientStmt.executeUpdate();
                            }

                            JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                ex.printStackTrace();
            }

            nomField.setText("");
            prenomField.setText("");
            emailField.setText("");
            phoneField.setText("");
            usernameField.setText("");
            passwordField.setText("");
        });

        retourBtn.addActionListener(e -> {
            // new GererClientApp(); // à décommenter si tu as cette classe
            dispose();
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(ajouterBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(retourBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AjouterClient());
    }
}
