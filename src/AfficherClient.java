import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class AfficherClient extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public AfficherClient() {
        setTitle("Liste des Clients 📋");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel title = new JLabel("Liste des Clients 👥", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(45, 45, 45));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(45, 45, 45));

        JLabel searchLabel = new JLabel("🔍 Rechercher username : ");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(200, 30));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Tableau sans colonne mot de passe
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Nom", "Prénom", "Téléphone", "Username"
        }, 0);

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton retour
        JButton retourBtn = new JButton("Retour 🔙");
        retourBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        retourBtn.setBackground(new Color(34, 177, 76));
        retourBtn.setForeground(Color.WHITE);
        retourBtn.setFocusPainted(false);
        retourBtn.setPreferredSize(new Dimension(120, 40));
        retourBtn.addActionListener(e -> {
            new GererClientApp(); // Assure-toi que cette classe existe
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.add(retourBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Charger les clients au départ
        chargerClients("");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String username = searchField.getText();
                chargerClients(username);
            }
        });

        setVisible(true);
    }

    private void chargerClients(String usernameSearch) {
        tableModel.setRowCount(0); // Vider le tableau

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            String query = """
                    SELECT c.id_client, c.nom, c.prenom, u.telephone, u.username
                    FROM client c
                    JOIN users u ON u.id = c.id_client
                    WHERE u.username LIKE ?
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + usernameSearch + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id_client");
                        String nom = rs.getString("nom");
                        String prenom = rs.getString("prenom");
                        String tel = rs.getString("telephone");
                        String username = rs.getString("username");

                        tableModel.addRow(new Object[]{id, nom, prenom, tel, username});
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AfficherClient());
    }
}
