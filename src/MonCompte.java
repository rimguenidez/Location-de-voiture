import javax.swing.*;
import java.awt.*;

public class MonCompte extends JPanel {
    private int clientId;
    private String username;

    public MonCompte(int clientId, String username) {
        this.clientId = clientId;
        this.username = username;

        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Titre
        JLabel titleLabel = new JLabel("💼 Mon Compte de " + username);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 40, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // Espace avec le footer

        JButton penaltyButton = createStyledButton("💸 Voir mes pénalités");
        penaltyButton.addActionListener(e -> openPenalitePage());

        JButton historyButton = createStyledButton("📜 Voir mon historique de locations");
        historyButton.addActionListener(e -> openHistoriquePage());

        JButton menuButton = createStyledButton("🏠 Retour au menu");
        menuButton.addActionListener(e -> openMainMenu());

        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(penaltyButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(historyButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(menuButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        footerPanel.setBackground(Color.DARK_GRAY);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 110));

        footerPanel.add(createFooterItem("🚗", "Voitures neuves"));
        footerPanel.add(createFooterItem("⏰", "Service 24h/24"));
        footerPanel.add(createFooterItem("💰", "Tarifs abordables"));

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        button.setBackground(new Color(0, 153, 76)); // Vert
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(350, 50)); // Taille réduite
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        return button;
    }

    private void openPenalitePage() {
        JFrame penaltyFrame = new JFrame("Pénalités de " + username);
        penaltyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        penaltyFrame.setSize(800, 500);
        penaltyFrame.setLocationRelativeTo(null);
        penaltyFrame.setContentPane(new Penalite(clientId, username));
        penaltyFrame.setVisible(true);
    }

    private void openHistoriquePage() {
        JFrame historyFrame = new JFrame("Historique des Locations de " + username);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(800, 500);
        historyFrame.setLocationRelativeTo(null);
        historyFrame.setContentPane(new Historique(username, clientId));
        historyFrame.setVisible(true);
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Menu Principal");
        mainMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainMenuFrame.setSize(800, 500);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setContentPane(new MainMenuAccueil());
        mainMenuFrame.setVisible(true);
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

    // Test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int testClientId = 1;
            String testUsername = "test_user";

            JFrame frame = new JFrame("Mon Compte");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MonCompte(testClientId, testUsername));
            frame.setVisible(true);
        });
    }
}
