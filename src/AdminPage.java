import javax.swing.*;
import java.awt.*;

public class AdminPage extends JFrame {

    private String username;

    public AdminPage(String username) {
        this.username = username;

        setTitle("Page d'administration 🚀");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titleLabel = new JLabel("Bienvenue " + username + " 🛠️");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JLabel subTitle = new JLabel("Espace Admin");
        subTitle.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subTitle.setForeground(Color.LIGHT_GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton manageClientsButton = new JButton("Gérer les clients 👥");
        JButton manageCarsButton = new JButton("Gérer les voitures 🚗");
        JButton statisticsButton = new JButton("Statistiques 📊");
        JButton menuButton = new JButton("Menu 🏠");
        JButton logoutButton = new JButton("Déconnexion 🚪");

        JButton[] buttons = {manageClientsButton, manageCarsButton, statisticsButton, menuButton, logoutButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 45));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setMargin(new Insets(8, 8, 8, 8));
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

        Color redColor = new Color(178, 34, 34);
        menuButton.setBackground(redColor);
        logoutButton.setBackground(redColor);

        for (JButton button : new JButton[]{manageClientsButton, manageCarsButton, statisticsButton}) {
            button.setBackground(new Color(50, 50, 50));
        }

        manageClientsButton.addActionListener(e -> {
            new GererClientApp();
            dispose();
        });

        manageCarsButton.addActionListener(e -> {
            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            GererVoitures gv = new GererVoitures(cardLayout, container, username); // si GererVoitures accepte ces paramètres
            container.add(gv, "GererVoitures");
            cardLayout.show(container, "GererVoitures");

            JFrame frame = new JFrame("Gestion des Voitures 🚗");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(750, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(container);
            frame.setVisible(true);

            dispose();
        });

        statisticsButton.addActionListener(e -> {
            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);

            Statistiques statistiques = new Statistiques(layout, container, username); // username passé ici
            container.add(statistiques, "stats");
            layout.show(container, "stats");

            JFrame frame = new JFrame("Statistiques 📊");
            frame.setContentPane(container);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            dispose();
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
        mainPanel.add(manageClientsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(manageCarsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(statisticsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(menuButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        footerPanel.setBackground(Color.DARK_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

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
        SwingUtilities.invokeLater(() -> new AdminPage("AdminTest"));
    }
}
