import javax.swing.*;
import java.awt.*;

public class ClientPage extends JFrame {
    private int clientId;
    private String username;

    public ClientPage(String username, int clientId) {
        this.username = username;
        this.clientId = clientId;

        setTitle("Page Client 🚗");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titleLabel = new JLabel("Bienvenue, " + username + " (ID Client: " + clientId + ") 👤");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel subTitle = new JLabel("Espace Client");
        subTitle.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subTitle.setForeground(Color.LIGHT_GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton louerVoitureButton = new JButton("🚗 Louer une voiture");
        JButton retournerVoitureButton = new JButton("🔁 Retourner une voiture");
        JButton monCompteButton = new JButton("👤 Mon Compte");
        JButton menuButton = new JButton("Menu 🏠");
        JButton logoutButton = new JButton("Déconnexion 🚪");

        JButton[] mainButtons = {louerVoitureButton, retournerVoitureButton, monCompteButton};
        for (JButton button : mainButtons) {
            button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 50));
            button.setBackground(new Color(50, 50, 50));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        Color redColor = new Color(178, 34, 34);
        JButton[] redButtons = {menuButton, logoutButton};
        for (JButton redBtn : redButtons) {
            redBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            redBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            redBtn.setMaximumSize(new Dimension(300, 50));
            redBtn.setBackground(redColor);
            redBtn.setForeground(Color.WHITE);
            redBtn.setFocusPainted(false);
            redBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            redBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        louerVoitureButton.addActionListener(e -> {
            JFrame louerFrame = new JFrame("Louer une voiture 🚘");
            louerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            louerFrame.setSize(800, 600);
            louerFrame.setResizable(false);
            louerFrame.setLocationRelativeTo(null);

            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);
            GererLocationsClient louerPanel = new GererLocationsClient(layout, container, clientId, username);

            container.add(louerPanel, "Louer");
            layout.show(container, "Louer");

            louerFrame.setContentPane(container);
            louerFrame.setVisible(true);
        });

        retournerVoitureButton.addActionListener(e -> {
            JFrame retourFrame = new JFrame("Retourner une voiture 🔁");
            retourFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            retourFrame.setSize(800, 600);
            retourFrame.setResizable(false);
            retourFrame.setLocationRelativeTo(null);

            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);
            GererRetourClient retourPanel = new GererRetourClient(layout, container, clientId, username);

            container.add(retourPanel, "Retour");
            layout.show(container, "Retour");

            retourFrame.setContentPane(container);
            retourFrame.setVisible(true);
        });

        monCompteButton.addActionListener(e -> {
            JFrame monCompteFrame = new JFrame("Mon Compte - Pénalités");
            monCompteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            monCompteFrame.setSize(800, 600);
            monCompteFrame.setLocationRelativeTo(null);

            MonCompte monComptePanel = new MonCompte(clientId, username);

            monCompteFrame.setContentPane(monComptePanel);
            monCompteFrame.setVisible(true);
        });

        menuButton.addActionListener(e -> {
            new MainMenuAccueil();
            dispose();
        });

        logoutButton.addActionListener(e -> {
            System.exit(0);
        });

        mainPanel.add(titleLabel);
        mainPanel.add(subTitle);
        mainPanel.add(louerVoitureButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(retournerVoitureButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(monCompteButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(menuButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientPage("usernameExample", 1));
    }
}
