import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ModifierClient extends JFrame {
    private JTextField idField, nomField, prenomField, emailField, phoneField, usernameField, passwordField;

    public ModifierClient() {
        setTitle("Modifier un Client ✏️");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Titre
        JLabel title = new JLabel("Modifier un Client 🛠️");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // Espacement entre les composants

        // Labels
        JLabel idLabel = new JLabel("ID Client :");
        JLabel nomLabel = new JLabel("Nom 🏷️ :");
        JLabel prenomLabel = new JLabel("Prénom 🏷️ :");
        JLabel emailLabel = new JLabel("Email 📧 :");
        JLabel phoneLabel = new JLabel("Téléphone 📱 :");
        JLabel usernameLabel = new JLabel("Nom d'utilisateur 👤 :");
        JLabel passwordLabel = new JLabel("Mot de passe 🔒 :");

        // Champs de texte
        idField = new JTextField(20);
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);

        // Taille des champs
        Dimension fieldSize = new Dimension(300, 35);
        idField.setPreferredSize(fieldSize);
        nomField.setPreferredSize(fieldSize);
        prenomField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        phoneField.setPreferredSize(fieldSize);
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        // Font des labels
        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 18);
        idLabel.setFont(labelFont);
        nomLabel.setFont(labelFont);
        prenomLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);

        // Couleur des labels
        idLabel.setForeground(Color.WHITE);
        nomLabel.setForeground(Color.WHITE);
        prenomLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        phoneLabel.setForeground(Color.WHITE);
        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        // Ajouter les composants au formulaire avec contraintes de mise en page
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(nomLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(nomField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(prenomLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(prenomField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(passwordField, gbc);

        // Boutons
        JButton modifierBtn = new JButton("Mettre à jour ✅");
        modifierBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        modifierBtn.setBackground(new Color(0, 123, 255));
        modifierBtn.setForeground(Color.WHITE);
        modifierBtn.setFocusPainted(false);
        modifierBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifierBtn.setMaximumSize(new Dimension(300, 40));

        JButton retourBtn = new JButton("Retour 🔙");
        retourBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        retourBtn.setBackground(new Color(34, 177, 76));
        retourBtn.setForeground(Color.WHITE);
        retourBtn.setFocusPainted(false);
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.setMaximumSize(new Dimension(300, 40));

        // Action du bouton "Mettre à jour"
        modifierBtn.addActionListener((ActionEvent e) -> {
            String id = idField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Vérification que l'ID est valide
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "L'ID du client est requis !");
                return;
            }

            // Vérification de l'unicité du username
            if (!username.isEmpty()) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
                    String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE username = ? AND id != ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkUsernameQuery)) {
                        checkStmt.setString(1, username);
                        checkStmt.setInt(2, Integer.parseInt(id));

                        ResultSet rs = checkStmt.executeQuery();
                        if (rs.next() && rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, "Le nom d'utilisateur est déjà pris !");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la vérification du nom d'utilisateur : " + ex.getMessage());
                    return;
                }
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
                // Préparer l'instruction UPDATE pour la table client
                String updateClient = "UPDATE client SET ";
                boolean first = true;

                if (!nom.isEmpty()) {
                    updateClient += "nom = ?";
                    first = false;
                }
                if (!prenom.isEmpty()) {
                    if (!first) updateClient += ", ";
                    updateClient += "prenom = ?";
                    first = false;
                }
                updateClient += " WHERE id_client = ?";

                try (PreparedStatement stmt = conn.prepareStatement(updateClient)) {
                    int index = 1;

                    if (!nom.isEmpty()) stmt.setString(index++, nom);
                    if (!prenom.isEmpty()) stmt.setString(index++, prenom);

                    stmt.setInt(index, Integer.parseInt(id));
                    stmt.executeUpdate();
                }

                // Préparer l'instruction UPDATE pour la table users
                String updateUser = "UPDATE users SET ";
                first = true;

                if (!email.isEmpty()) {
                    updateUser += "email = ?";
                    first = false;
                }
                if (!phone.isEmpty()) {
                    if (!first) updateUser += ", ";
                    updateUser += "telephone = ?";
                    first = false;
                }
                if (!username.isEmpty()) {
                    if (!first) updateUser += ", ";
                    updateUser += "username = ?";
                    first = false;
                }
                if (!password.isEmpty()) {
                    if (!first) updateUser += ", ";
                    updateUser += "password = ?";
                    first = false;
                }
                updateUser += " WHERE id = ?";

                try (PreparedStatement stmt = conn.prepareStatement(updateUser)) {
                    int index = 1;

                    if (!email.isEmpty()) stmt.setString(index++, email);
                    if (!phone.isEmpty()) stmt.setString(index++, phone);
                    if (!username.isEmpty()) stmt.setString(index++, username);
                    if (!password.isEmpty()) stmt.setString(index++, password);

                    stmt.setInt(index, Integer.parseInt(id));
                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Client mis à jour avec succès !");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        // Action du bouton "Retour"
        retourBtn.addActionListener(e -> {
            new GererClientApp(); // Retour à la page principale
            dispose();
        });

        // Ajouter les composants au panel
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(modifierBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(retourBtn);

        // Ajouter au JFrame
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModifierClient());
    }
}
