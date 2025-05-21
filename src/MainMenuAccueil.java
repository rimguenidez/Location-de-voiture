import javax.swing.*;
import java.awt.*;

public class MainMenuAccueil extends JFrame {
   
    public MainMenuAccueil() {

        setTitle("Bienvenue dans notre service de location 🚗");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titleLabel = new JLabel("Bienvenue dans notre service de location 🚗");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JButton loginButton = new JButton("Se connecter 🔐");
        JButton signUpButton = new JButton("Créer un compte 📝");
        JButton aboutButton = new JButton("À propos ℹ️");
        JButton quitButton = new JButton("Quitter ❌");

        // Style boutons normaux
        for (JButton button : new JButton[]{loginButton, signUpButton, aboutButton}) {
            button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 50));
            button.setBackground(new Color(50, 50, 50));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        // Style bouton quitter
        quitButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setMaximumSize(new Dimension(300, 50));
        quitButton.setBackground(Color.RED);
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        
        quitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Les Actions
        loginButton.addActionListener(e -> {
            new LoginForm().setVisible(true); 
            dispose();
        });

        signUpButton.addActionListener(e -> new SignUpForm().setVisible(true)); 

        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Contact : +216 23 456 789\nEmail : contact@voitures.com\nDéveloppé par: Rim Guenidez, Mohamed Amine Nebli et Rayen Guedri",
                "À propos", JOptionPane.INFORMATION_MESSAGE));

        quitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(titleLabel);
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(signUpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(aboutButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(quitButton);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuAccueil::new);
    }
}
