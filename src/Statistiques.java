import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Statistiques extends JPanel {
    private Connection c;
    private CardLayout cardLayout;
    private JPanel container;

    public Statistiques(CardLayout layout, JPanel cont) {
        this.cardLayout = layout;
        this.container = cont;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Titre
        JLabel titleLabel = new JLabel("📊 Statistiques");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton btnEtatVoitures = createStyledButton("🚘 Voitures & États");
        JButton btnDisponibilite = createStyledButton("📅 Disponibles / Louées");
        JButton btnPlusDemandees = createStyledButton("🔥 Top 3 Voitures");
        JButton btnClientRegulier = createStyledButton("👤 Client Régulier");

        buttonPanel.add(btnEtatVoitures);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnDisponibilite);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnPlusDemandees);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnClientRegulier);

        add(buttonPanel, BorderLayout.CENTER);

        // Actions des boutons
        btnEtatVoitures.addActionListener(e -> affichTab("Voitures & États",
                "SELECT immatriculation, etat FROM voiture",
                new String[]{"Immatriculation", "État"}));

        btnDisponibilite.addActionListener(e -> affichTab("Voitures Disponibles / Louées",
                "SELECT immatriculation, disponible FROM voiture",
                new String[]{"Immatriculation", "Disponible"}));

        btnPlusDemandees.addActionListener(e -> affichTab("Top 3 Voitures les plus demandées",
                "SELECT immatriculation, COUNT(*) AS nb_loc FROM location GROUP BY immatriculation ORDER BY nb_loc DESC LIMIT 3",
                new String[]{"Voiture", "Nombre de locations"}));

        btnClientRegulier.addActionListener(e -> affichTab("Client Régulier",
                "SELECT id_client, COUNT(*) AS nb_locations FROM location GROUP BY id_client ORDER BY nb_locations DESC LIMIT 1",
                new String[]{"ID Client", "Nombre de locations"}));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        button.setBackground(new Color(0, 153, 76)); // Vert
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        return button;
    }

    private void affichTab(String title, String query, String[] colonnes) {
        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            DefaultTableModel model = new DefaultTableModel(colonnes, 0);
            while (rs.next()) {
                Object[] row = new Object[colonnes.length];
                for (int i = 0; i < colonnes.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lancement de test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Statistiques");
            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);
            Statistiques statsPanel = new Statistiques(layout, container);
            container.add(statsPanel, "stats");
            frame.setContentPane(container);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
