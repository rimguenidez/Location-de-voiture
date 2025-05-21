import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GererLocationsClient extends JPanel {
    private JTextField txtIdClient, txtDateDebut, txtDateFin, txtSearch;
    private JComboBox<String> comboVoitures;
    private Connection connection;
    private List<String> voituresDisponibles;
    private List<String> voituresFiltrees;
    private CardLayout layout;
    private JPanel container;
    private String username; // Store the username

    public GererLocationsClient(CardLayout layout, JPanel container, int idClient, String username) {
        this.layout = layout;
        this.container = container;
        this.username = username; // Set the username
        this.voituresDisponibles = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Panel for the greeting message (Styled like GererRetourClient)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrer le texte
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20)); // Adjust margin as needed

        // Add greeting label
        JLabel welcomeLabel = new JLabel("Bienvenue, " + username + " 👤");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22)); // Agrandir la taille de la police
        topPanel.add(welcomeLabel);

        // Main form panel
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        panel.add(label("Id Client :"));
        txtIdClient = new JTextField(String.valueOf(idClient));
        txtIdClient.setEditable(false); // The user cannot modify it
        panel.add(txtIdClient);

        panel.add(label("Date Début (yyyy-MM-dd) :"));
        txtDateDebut = new JTextField();
        panel.add(txtDateDebut);

        panel.add(label("Date Fin (yyyy-MM-dd) :"));
        txtDateFin = new JTextField();
        panel.add(txtDateFin);

        panel.add(label("🔍 Rechercher voiture :"));
        txtSearch = new JTextField();
        panel.add(txtSearch);

        panel.add(label("Voitures disponibles :"));
        comboVoitures = new JComboBox<>();
        panel.add(comboVoitures);

        JButton louer = new JButton("Louer");
        JButton menu = new JButton("Menu");

        setButtonStyle(louer);
        setButtonStyle(menu);

        // Button Menu - red color
        menu.setBackground(new Color(178, 34, 34)); // dark red
        menu.setForeground(Color.WHITE);
        menu.setPreferredSize(new Dimension(150, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(louer);
        buttonPanel.add(menu);

        add(topPanel, BorderLayout.NORTH); // Add the greeting panel
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        chargerToutesVoituresDisponibles();
        afficherVoituresDansCombo(voituresDisponibles);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrerVoitures(); }
            public void removeUpdate(DocumentEvent e) { filtrerVoitures(); }
            public void changedUpdate(DocumentEvent e) { filtrerVoitures(); }
        });

        louer.addActionListener(e -> louerVoiture());
        menu.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new ClientPage(username,idClient).setVisible(true);
        });
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void setButtonStyle(JButton btn) {
        btn.setBackground(new Color(34, 139, 34));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(150, 40));
    }

    private void chargerToutesVoituresDisponibles() {
        voituresDisponibles = new ArrayList<>();
        try {
            String sql = "SELECT * FROM voiture WHERE disponible = TRUE AND etat = 'en_marche'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String info = rs.getString("immatriculation") + " - " + rs.getString("marque") + " " +
                        rs.getString("modele") + " - " + rs.getString("couleur");
                voituresDisponibles.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des voitures.");
        }
    }

    private void afficherVoituresDansCombo(List<String> voitures) {
        comboVoitures.removeAllItems();
        for (String v : voitures) {
            comboVoitures.addItem(v);
        }
    }

    private void filtrerVoitures() {
        String searchText = txtSearch.getText().toLowerCase();
        voituresFiltrees = voituresDisponibles.stream()
                .filter(v -> v.toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        afficherVoituresDansCombo(voituresFiltrees);
    }

    private void louerVoiture() {
        try {
            int idClient = Integer.parseInt(txtIdClient.getText());
            if (!verifClient(idClient)) return;

            if (comboVoitures.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Aucune voiture sélectionnée !");
                return;
            }

            String voitureInfo = (String) comboVoitures.getSelectedItem();
            String immatriculation = voitureInfo.split(" - ")[0];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dateDebut = sdf.parse(txtDateDebut.getText());
            java.util.Date dateFin = sdf.parse(txtDateFin.getText());

            long dureeMillis = dateFin.getTime() - dateDebut.getTime();
            long dureeJours = dureeMillis / (1000 * 60 * 60 * 24);

            String sql = "INSERT INTO location (immatriculation, id_client, date_debut, date_fin) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, immatriculation);
            pstmt.setInt(2, idClient);
            pstmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
            pstmt.setDate(4, new java.sql.Date(dateFin.getTime()));

            int result = pstmt.executeUpdate();
            if (result > 0) {
                String updateEtat = "UPDATE voiture SET disponible = FALSE WHERE immatriculation = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateEtat);
                updateStmt.setString(1, immatriculation);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Voiture louée pour " + dureeJours + " jours !");
                chargerToutesVoituresDisponibles();
                filtrerVoitures();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la location.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la location.");
        }
    }

    private boolean verifClient(int idClient) throws SQLException {
        String verif = "SELECT * FROM client WHERE id_client=?";
        PreparedStatement pverif = connection.prepareStatement(verif);
        pverif.setInt(1, idClient);
        ResultSet rverif = pverif.executeQuery();
        if (rverif.next()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Client avec cet ID n'existe pas !");
            return false;
        }
    }

    // Méthode de test uniquement
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Location Client 🚘");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);

            // Remplace "1" par un ID client valide pour tester
            String username = "JeanDupont"; // Test username
            GererLocationsClient panel = new GererLocationsClient(layout, container, 1, username);
            container.add(panel, "Client");

            layout.show(container, "Client");
            frame.setContentPane(container);
            frame.setVisible(true);
        });
    }
}
