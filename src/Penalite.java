import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Penalite extends JPanel {
    private int clientId;
    private String username;

    public Penalite(int clientId, String username) {
        this.clientId = clientId;
        this.username = username;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("💸 Mon Compte - Pénalités de " + username);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 40, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central avec mise en page verticale
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(30, 30, 30));

        // recuperation des pénalités
        double totalPenalties = getTotalPenalties();

        JLabel penaltyLabel = new JLabel("💰 Montant des pénalités : ");
        penaltyLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        penaltyLabel.setForeground(Color.WHITE);

        JLabel penaltyAmount = new JLabel(totalPenalties > 0 ? totalPenalties + " DNT" : "✅ Aucune pénalité à payer !");
        penaltyAmount.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        penaltyAmount.setForeground(totalPenalties > 0 ? Color.RED : new Color(0, 200, 0));

        JPanel penaltyInfoPanel = new JPanel();
        penaltyInfoPanel.setBackground(new Color(30, 30, 30));
        penaltyInfoPanel.add(penaltyLabel);
        penaltyInfoPanel.add(penaltyAmount);
        penaltyInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        centerPanel.add(penaltyInfoPanel);
        centerPanel.add(Box.createVerticalStrut(30));

        JLabel paymentLabel = new JLabel("💳 Choisissez une méthode de paiement :");
        paymentLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        paymentLabel.setForeground(Color.WHITE);
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel paymentPanel = new JPanel();
        paymentPanel.setBackground(new Color(30, 30, 30));
        paymentPanel.add(paymentLabel);

        centerPanel.add(paymentPanel);

        JButton chequeButton = createPaymentButton("🧾 Chèque");
        chequeButton.addActionListener(e -> processPayment("Chèque"));

        JButton cashButton = createPaymentButton("💵 Cash");
        cashButton.addActionListener(e -> processPayment("Cash"));

        JButton cardButton = createPaymentButton("💳 Carte");
        cardButton.addActionListener(e -> processPayment("Carte bancaire"));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonRow.setBackground(new Color(30, 30, 30));
        buttonRow.add(chequeButton);
        buttonRow.add(cashButton);
        buttonRow.add(cardButton);

        centerPanel.add(buttonRow);

        // Bouton rouge "Retour"
        JButton backButton = new JButton("↩️ Retour");
        backButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose(); // ferme la fenêtre actuelle
            new MonCompte(clientId, username); // appelle la page MonCompte
        });

        JPanel backPanel = new JPanel();
        backPanel.setBackground(new Color(30, 30, 30));
        backPanel.add(backButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(backPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        footerPanel.setBackground(new Color(30, 30, 30));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 120));
        footerPanel.add(createFooterItem("🚗", "Voitures neuves"));
        footerPanel.add(createFooterItem("⏰", "Service 24h/24"));
        footerPanel.add(createFooterItem("💰", "Tarifs abordables"));
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createFooterItem(String emoji, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 30));
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

    private JButton createPaymentButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        button.setBackground(new Color(0, 153, 76));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private double getTotalPenalties() {
        String url = "jdbc:mysql://localhost:3306/bdproject";
        String user = "root";
        String password = "12chocolate";
        double totalPenalties = 0;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = """
                SELECT SUM(penalite) AS total_penalite
                FROM location
                WHERE id_client = ? AND penalite > 0
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, clientId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalPenalties = rs.getDouble("total_penalite");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Erreur lors du chargement des pénalités.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return totalPenalties;
    }

    private void processPayment(String method) {
        double totalPenalties = getTotalPenalties();
        if (totalPenalties > 0) {
            int option = JOptionPane.showConfirmDialog(this,
                    "🔒 Êtes-vous sûr de vouloir payer " + totalPenalties + " DNT par " + method + " ?",
                    "Confirmation de paiement", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                markPenaltiesAsPaid();
                JOptionPane.showMessageDialog(this, "✅ Paiement effectué par " + method + " !", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "✅ Aucune pénalité à payer.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markPenaltiesAsPaid() {
        String url = "jdbc:mysql://localhost:3306/bdproject";
        String user = "root";
        String password = "12chocolate";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String updateQuery = """
                UPDATE location
                SET penalite = 0
                WHERE id_client = ? AND penalite > 0
            """;

            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, clientId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Erreur lors de la mise à jour du paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int testClientId = 1;
            String testUsername = "test_user";
            JFrame frame = new JFrame("Mon Compte - Pénalités");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Penalite(testClientId, testUsername));
            frame.setVisible(true);
        });
    }
}
