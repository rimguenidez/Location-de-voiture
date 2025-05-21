import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class Statistiques extends JPanel {
    private Connection c;
    private CardLayout cardLayout;
    private JPanel container;
    private JButton btnClientRegulier;

    public Statistiques(CardLayout layout, JPanel cont, String role) {
        this.cardLayout = layout;
        this.container = cont;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        btnClientRegulier = createStyledButton("👤 Client Régulier");

        buttonPanel.add(btnEtatVoitures);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnDisponibilite);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnPlusDemandees);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnClientRegulier); // Bouton visible par défaut

        add(buttonPanel, BorderLayout.CENTER);

        // Actions 
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
                "SELECT c.id_client, c.nom, c.prenom, COUNT(l.id_client) AS nb_locations " +
                        "FROM client c " +
                        "JOIN location l ON c.id_client = l.id_client " +
                        "GROUP BY c.id_client, c.nom, c.prenom " +
                        "ORDER BY nb_locations DESC " +
                        "LIMIT 1",
                new String[]{"ID Client", "Nom", "Prénom", "Nombre de locations"}));

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
            styleTable(table);

            // pour afficher le tableau
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), title, Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(new Color(34, 34, 34));

            JLabel dialogTitle = new JLabel(title, SwingConstants.CENTER);
            dialogTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
            dialogTitle.setForeground(new Color(0, 153, 76));
            dialogTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            dialog.add(dialogTitle, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(new Color(34, 34, 34));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            dialog.add(scrollPane, BorderLayout.CENTER);

            // Bouton fermer 
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(34, 34, 34));
            JButton closeButton = new JButton("Fermer");
            closeButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            closeButton.setBackground(new Color(178, 34, 34)); // Rouge foncé
            closeButton.setForeground(Color.WHITE);
            closeButton.setFocusPainted(false);
            closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            closeButton.setPreferredSize(new Dimension(120, 40));
            closeButton.addActionListener(ev -> dialog.dispose());
            buttonPanel.add(closeButton);

            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(34, 34, 34));
        table.setRowHeight(30);
        table.setGridColor(new Color(64, 64, 64));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(0, 153, 76));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 153, 76));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Statistiques");
            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);

            String roleUtilisateur = "admin"; // Le rôle n'a plus d'importance ici
            Statistiques panel = new Statistiques(layout, container, roleUtilisateur);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.setSize(700, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
