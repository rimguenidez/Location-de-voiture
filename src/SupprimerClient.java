import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;

public class SupprimerClient extends JFrame {
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    public SupprimerClient() {
        setTitle("Supprimer un Client ❌");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(45, 45, 45));

        JLabel searchLabel = new JLabel("Rechercher par username :");
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchLabel.setForeground(Color.WHITE);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        // Recherche instantanée
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchClients();
            }

            public void removeUpdate(DocumentEvent e) {
                searchClients();
            }

            public void changedUpdate(DocumentEvent e) {
                searchClients();
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Tableau
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Prénom", "Username", "Email"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        table.setRowHeight(25);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);

        // Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(45, 45, 45));

        JButton deleteBtn = new JButton("Supprimer le client sélectionné ❌");
        JButton retourBtn = new JButton("Retour 🔙");

        deleteBtn.setBackground(new Color(200, 0, 0));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        deleteBtn.setFocusPainted(false);

        retourBtn.setBackground(new Color(34, 177, 76));
        retourBtn.setForeground(Color.WHITE);
        retourBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        retourBtn.setFocusPainted(false);

        deleteBtn.addActionListener(e -> deleteSelectedClient());
        retourBtn.addActionListener(e -> {
            new GererClientApp();
            dispose();
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(retourBtn);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);

        // Charger tous les clients au démarrage
        searchClients();
    }

    private void searchClients() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0); // Vider le tableau

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            String query = "SELECT u.id, c.nom, c.prenom, u.username, u.email " +
                           "FROM users u JOIN client c ON u.id = c.id_client " +
                           "WHERE u.role = 'client' AND u.username LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("username"),
                        rs.getString("email")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
        }
    }

    private void deleteSelectedClient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {

                // Supprimer locations
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM location WHERE id_client = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }

                // Supprimer client
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM client WHERE id_client = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }

                // Supprimer user
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Client supprimé avec succès !");
                tableModel.removeRow(selectedRow);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SupprimerClient::new);
    }
}
