import javax.swing.*;
import java.awt.*;

public class GererClientApp extends JFrame {

    public GererClientApp() {
        setTitle("Gestion des Clients 👥");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Gestion des Clients 👥");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton ajouterBtn = new JButton("Ajouter un client ➕");
        JButton modifierBtn = new JButton("Modifier un client ✏️");
        JButton supprimerBtn = new JButton("Supprimer un client ❌");
        JButton afficherBtn = new JButton("Afficher tous les clients 📋");
        JButton retourBtn = new JButton("Retour au menu 🏠");

        JButton[] buttons = {ajouterBtn, modifierBtn, supprimerBtn, afficherBtn, retourBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            btn.setMaximumSize(new Dimension(300, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(new Color(50, 50, 50));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        retourBtn.setBackground(new Color(178, 34, 34)); // Rouge

        retourBtn.addActionListener(e -> {
            new AdminPage("Admin"); // Tu peux passer le vrai nom d’utilisateur ici
            dispose();
        });

        ajouterBtn.addActionListener(e -> new AjouterClient());
        modifierBtn.addActionListener(e -> new ModifierClient());
        supprimerBtn.addActionListener(e -> new SupprimerClient());
        afficherBtn.addActionListener(e -> new AfficherClient());

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(ajouterBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(modifierBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(supprimerBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(afficherBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(retourBtn);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GererClientApp());
    }
}
