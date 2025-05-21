import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GererVoitures extends JPanel {
    private JTextField txtImmatriculation, txtMarque, txtModele;
    private CardLayout cardLayout;
    private JPanel container;
    private JComboBox<String> cmbEtat, cmbCouleur;
    private JCheckBox chkDisponible;
    private Connection connection;
    private String username;

    public GererVoitures(CardLayout layout, JPanel cont, String username) {
        this.username = username;
        this.cardLayout = layout;
        this.container = cont;

        setSize(700, 650);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 35, 35));

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(40, 40, 40));

        Font font = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        Color labelColor = Color.WHITE;
        Color fieldBg = Color.WHITE;
        Color fieldFg = Color.BLACK;

        JLabel lbl1 = new JLabel("Immatriculation :");
        JLabel lbl2 = new JLabel("Marque :");
        JLabel lbl3 = new JLabel("Modèle :");
        JLabel lbl4 = new JLabel("État :");
        JLabel lbl5 = new JLabel("Disponible :");
        JLabel lbl6 = new JLabel("Couleur :");

        for (JLabel lbl : new JLabel[]{lbl1, lbl2, lbl3, lbl4, lbl5, lbl6}) {
            lbl.setForeground(labelColor);
            lbl.setFont(font);
        }

        txtImmatriculation = new JTextField();
        txtMarque = new JTextField();
        txtModele = new JTextField();
        cmbEtat = new JComboBox<>(new String[]{"en_marche", "en_panne"});
        cmbCouleur = new JComboBox<>(new String[]{"Rouge", "Bleu", "Vert", "Noir", "Blanc", "Gris", "Jaune"});
        chkDisponible = new JCheckBox();
        chkDisponible.setPreferredSize(new Dimension(25, 25));

        JTextField[] textFields = {txtImmatriculation, txtMarque, txtModele};
        for (JTextField tf : textFields) {
            tf.setBackground(fieldBg);
            tf.setForeground(fieldFg);
            tf.setCaretColor(Color.BLACK);
            tf.setFont(font);
            tf.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        cmbEtat.setBackground(fieldBg);
        cmbEtat.setForeground(fieldFg);
        cmbEtat.setFont(font);
        cmbCouleur.setBackground(fieldBg);
        cmbCouleur.setForeground(fieldFg);
        cmbCouleur.setFont(font);
        chkDisponible.setBackground(formPanel.getBackground());

        formPanel.add(createRow(lbl1, txtImmatriculation));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createRow(lbl2, txtMarque));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createRow(lbl3, txtModele));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createRow(lbl4, cmbEtat));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createRow(lbl6, cmbCouleur));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createRow(lbl5, chkDisponible));

        JButton ajouter = new JButton("➕ Ajouter");
        JButton modifier = new JButton("✏️ Modifier");
        JButton supprimer = new JButton("🗑️ Supprimer");
        JButton retour = new JButton("🔙 Retour");

        for (JButton btn : new JButton[]{ajouter, modifier, supprimer, retour}) {
            btn.setBackground(new Color(34, 139, 34));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            btn.setPreferredSize(new Dimension(130, 35));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(formPanel.getBackground());
        buttonPanel.add(ajouter);
        buttonPanel.add(modifier);
        buttonPanel.add(supprimer);
        buttonPanel.add(retour);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.CENTER);

        ajouter.addActionListener(e -> ajouterVoiture());
        modifier.addActionListener(e -> modifierVoiture());
        supprimer.addActionListener(e -> supprimerVoiture());

        retour.addActionListener(e -> {
            container.add(new AdminPage(username), "AdminPage");
            cardLayout.show(container, "AdminPage");
        });
    }

    private JPanel createRow(JLabel label, JComponent field) {
        JPanel labelFieldPanel = new JPanel();
        labelFieldPanel.setLayout(new BoxLayout(labelFieldPanel, BoxLayout.X_AXIS));
        labelFieldPanel.setBackground(new Color(55, 55, 55));
        labelFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        label.setPreferredSize(new Dimension(150, 30));
        labelFieldPanel.add(label);
        labelFieldPanel.add(Box.createHorizontalStrut(10));
        labelFieldPanel.add(field);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(40, 40, 40));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 5, 0),
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1, true)
        ));
        wrapper.add(labelFieldPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private void ajouterVoiture() {
        try {
            String immatriculation = txtImmatriculation.getText();
            String marque = txtMarque.getText();
            String modele = txtModele.getText();
            String etat = (String) cmbEtat.getSelectedItem();
            boolean disponible = chkDisponible.isSelected();
            String couleur = (String) cmbCouleur.getSelectedItem();

            String verif = "SELECT * FROM voiture WHERE immatriculation=?";
            PreparedStatement pverif = connection.prepareStatement(verif);
            pverif.setString(1, immatriculation);
            ResultSet rs = pverif.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Voiture avec cette immatriculation existe déjà !");
            } else {
                String sql = "INSERT INTO voiture(immatriculation, marque, modele, etat, disponible, couleur) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement p = connection.prepareStatement(sql);
                p.setString(1, immatriculation);
                p.setString(2, marque);
                p.setString(3, modele);
                p.setString(4, etat);
                p.setBoolean(5, disponible);
                p.setString(6, couleur);
                p.executeUpdate();
                JOptionPane.showMessageDialog(this, "Voiture ajoutée avec succès!");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierVoiture() {
        try {
            String immatriculation = txtImmatriculation.getText();
            String marque = txtMarque.getText();
            String modele = txtModele.getText();
            String etat = (String) cmbEtat.getSelectedItem();
            boolean disponible = chkDisponible.isSelected();
            String couleur = (String) cmbCouleur.getSelectedItem();

            String verif = "SELECT * FROM voiture WHERE immatriculation=?";
            PreparedStatement pverif = connection.prepareStatement(verif);
            pverif.setString(1, immatriculation);
            ResultSet rs = pverif.executeQuery();

            if (rs.next()) {
                String sql = "UPDATE voiture SET marque=?, modele=?, etat=?, disponible=?, couleur=? WHERE immatriculation=?";
                PreparedStatement p = connection.prepareStatement(sql);
                p.setString(1, marque);
                p.setString(2, modele);
                p.setString(3, etat);
                p.setBoolean(4, disponible);
                p.setString(5, couleur);
                p.setString(6, immatriculation);
                p.executeUpdate();
                JOptionPane.showMessageDialog(this, "Voiture modifiée avec succès!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Voiture avec cette immatriculation n'existe pas !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerVoiture() {
        try {
            String immatriculation = txtImmatriculation.getText();

            String verif = "SELECT * FROM voiture WHERE immatriculation=?";
            PreparedStatement pverif = connection.prepareStatement(verif);
            pverif.setString(1, immatriculation);
            ResultSet rs = pverif.executeQuery();

            if (rs.next()) {
                String sql = "DELETE FROM voiture WHERE immatriculation=?";
                PreparedStatement p = connection.prepareStatement(sql);
                p.setString(1, immatriculation);
                p.executeUpdate();
                JOptionPane.showMessageDialog(this, "Voiture supprimée avec succès!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Voiture introuvable !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtImmatriculation.setText("");
        txtMarque.setText("");
        txtModele.setText("");
        cmbEtat.setSelectedIndex(0);
        chkDisponible.setSelected(false);
        cmbCouleur.setSelectedIndex(0);
    }

    public String getusername() {
        return username;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestion des Voitures 🚗");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(750, 600);
            frame.setLocationRelativeTo(null);

            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);

            String username = "admin";
            GererVoitures gv = new GererVoitures(layout, container, username);

            container.add(gv, "GererVoitures");
            frame.setContentPane(container);
            layout.show(container, "GererVoitures");
            frame.setVisible(true);
        });
    }
}
