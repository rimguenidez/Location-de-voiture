import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class SupprimerClient extends JFrame {
    private JTextField idField;

    public SupprimerClient() {
        setTitle("Supprimer un Client ❌");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Supprimer un Client 🗑️");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel idLabel = new JLabel("ID Client à supprimer :");
        idLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField(20);
        idField.setPreferredSize(new Dimension(300, 35));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(idField, gbc);

        JButton supprimerBtn = new JButton("Supprimer ❌");
        supprimerBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        supprimerBtn.setBackground(new Color(200, 0, 0));
        supprimerBtn.setForeground(Color.WHITE);
        supprimerBtn.setFocusPainted(false);
        supprimerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        supprimerBtn.setMaximumSize(new Dimension(300, 40));

        JButton retourBtn = new JButton("Retour 🔙");
        retourBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        retourBtn.setBackground(new Color(34, 177, 76));
        retourBtn.setForeground(Color.WHITE);
        retourBtn.setFocusPainted(false);
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.setMaximumSize(new Dimension(300, 40));

        supprimerBtn.addActionListener((ActionEvent e) -> {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un ID valide.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce client et toutes ses locations ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {

                    // Vérifie si le client existe
                    String check = "SELECT * FROM client WHERE id_client = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
                        checkStmt.setInt(1, Integer.parseInt(id));
                        ResultSet rs = checkStmt.executeQuery();

                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(this, "Aucun client trouvé avec cet ID !");
                            return;
                        }
                    }

                    // Supprimer d'abord les locations
                    String deleteLocations = "DELETE FROM location WHERE id_client = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteLocations)) {
                        stmt.setInt(1, Integer.parseInt(id));
                        stmt.executeUpdate();
                    }

                    // Supprimer de la table client
                    String deleteClient = "DELETE FROM client WHERE id_client=?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteClient)) {
                        stmt.setInt(1, Integer.parseInt(id));
                        stmt.executeUpdate();
                    }

                    // Supprimer de la table users
                    String deleteUser = "DELETE FROM users WHERE id=?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteUser)) {
                        stmt.setInt(1, Integer.parseInt(id));
                        stmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this, "Client et ses locations supprimés avec succès !");
                    idField.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID invalide. Veuillez entrer un entier.");
                }
            }
        });

        retourBtn.addActionListener(e -> {
            new GererClientApp();
            dispose();
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(supprimerBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(retourBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupprimerClient());
    }
}
