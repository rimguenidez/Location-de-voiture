import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class SignUpForm extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, usernameField;
    private JPasswordField passwordField;

    public SignUpForm() {
        setTitle("Créer un compte ✍");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500); 
        setLocationRelativeTo(null);
        setResizable(true);

        // Couleur de fond du frame
        getContentPane().setBackground(new Color(60, 63, 65));
        setLayout(new BorderLayout());

        // Panel principal qui va centrer le formulaire
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        // Panel formulaire, largeur max 400px mais champs s'étendent
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(60, 63, 65));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createFieldPanel("👤 Prénom :", firstNameField = createTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("👤 Nom :", lastNameField = createTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("📧 Email :", emailField = createTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("📱 Téléphone :", phoneField = createTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("👤 Nom d'utilisateur :", usernameField = createTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("🔒 Mot de passe :", passwordField = createPasswordField()));

        
        formPanel.add(Box.createVerticalStrut(50));  

        JButton signUpButton = new JButton("✅ Créer le compte");
        signUpButton.setBackground(new Color(34, 139, 34));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        signUpButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Taille préférée et max pour agrandir le bouton
        signUpButton.setPreferredSize(new Dimension(180, 50));
        signUpButton.setMaximumSize(new Dimension(180, 50));

        signUpButton.addActionListener(e -> createAccount());

        formPanel.add(signUpButton);
        formPanel.add(Box.createVerticalGlue());

        // Positionnement dans le GridBagLayout pour centrer
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;   // prendre tout l'espace horizontal
        gbc.weighty = 1.0;   // prendre tout l'espace vertical
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(formPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        footerPanel.setBackground(Color.DARK_GRAY);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 120));

        footerPanel.add(createFooterItem("🚗", "Voitures neuves"));
        footerPanel.add(createFooterItem("⏰", "Service 24h/24"));
        footerPanel.add(createFooterItem("💰", "Tarifs abordables"));

        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createFooterItem(String emoji, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
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
    

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(60, 63, 65));

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(130, 24));

        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 24));
        field.setMargin(new Insets(3, 8, 3, 8));
        field.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        // Permet au champ de s'étendre horizontalement
        field.setMinimumSize(new Dimension(250, 24));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        // Pour que le panel prenne toute la largeur disponible
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        return tf;
    }

    private JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(Color.WHITE);
        pf.setForeground(Color.BLACK);
        return pf;
    }

    private void createAccount() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (phone.isEmpty() || !phone.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "Numéro de téléphone invalide (8 chiffres).", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            String query = "INSERT INTO users (username, password, role, email, telephone, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "client");
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, LocalDateTime.now().toString());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            if (userId != -1) {
                String clientQuery = "INSERT INTO client (id_client, nom, prenom) VALUES (?, ?, ?)";
                PreparedStatement clientStmt = conn.prepareStatement(clientQuery);
                clientStmt.setInt(1, userId);
                clientStmt.setString(2, lastName);
                clientStmt.setString(3, firstName);
                clientStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Compte créé avec succès 🎉 !");
            dispose();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpForm().setVisible(true));
    }
}
