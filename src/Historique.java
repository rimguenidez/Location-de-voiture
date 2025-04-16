import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Historique extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public Historique(String username, int idClient) {
        // Configuration du panel avec un fond élégant
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));  // Fond sombre pour une ambiance classe

        // Titre de l'historique
        JLabel titleLabel = new JLabel("Historique de Location de " + username);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0)); // Or pour le titre
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 40, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Création du tableau avec les colonnes appropriées
        String[] columnNames = {"Immatriculation", "Marque", "Modèle", "Date Début", "Date Fin", "Retour Effectif", "Pénalité"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        // Personnalisation du tableau
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Police élégante
        table.setBackground(new Color(50, 50, 50));  // Fond gris pour les lignes
        table.setForeground(Color.WHITE);  // Texte blanc pour contraster avec le fond sombre
        table.setSelectionBackground(new Color(0, 153, 76)); // Sélection verte élégante
        table.setSelectionForeground(Color.WHITE);  // Texte blanc lors de la sélection
        table.setGridColor(new Color(60, 60, 60));  // Couleur des grilles de tableau
        table.setRowHeight(35);  // Hauteur des lignes augmentée pour plus d'espace
        table.setShowGrid(true); // Afficher les grilles

        // Appliquer des bordures arrondies et une meilleure présentation
        table.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));

        // Pour une meilleure apparence de l'en-tête du tableau
        table.getTableHeader().setBackground(new Color(30, 30, 30));  // Fond sombre de l'en-tête
        table.getTableHeader().setForeground(Color.WHITE);  // Texte blanc pour l'en-tête
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));  // Police plus épaisse pour l'en-tête
        table.getTableHeader().setReorderingAllowed(false);  // Empêcher le réarrangement des colonnes

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Récupérer et afficher l'historique de location
        loadRentalHistory(username, idClient);
    }

    public void loadRentalHistory(String username, int idClient) {
        // Connexion à la base de données
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate")) {
            // Requête SQL pour récupérer l'historique de location du client
            String query = "SELECT v.immatriculation, v.marque, v.modele, l.date_debut, l.date_fin, l.retour_effectif, l.penalite " +
                           "FROM location l " +
                           "JOIN voiture v ON l.immatriculation = v.immatriculation " +
                           "WHERE l.id_client = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idClient);
                
                // Exécution de la requête
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Récupérer les données et les ajouter à la table
                        String immatriculation = resultSet.getString("immatriculation");
                        String marque = resultSet.getString("marque");
                        String modele = resultSet.getString("modele");
                        String dateDebut = resultSet.getString("date_debut");
                        String dateFin = resultSet.getString("date_fin");
                        String retourEffectif = resultSet.getString("retour_effectif");
                        String penalite = resultSet.getString("penalite");

                        // Si retour_effectif est null ou vide, afficher "en attente"
                        if (retourEffectif == null || retourEffectif.isEmpty()) {
                            retourEffectif = "En attente";
                        }

                        // Ajouter les données dans le tableau
                        Object[] row = {immatriculation, marque, modele, dateDebut, dateFin, retourEffectif, penalite};
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Historique des Locations");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Historique("test_user", 1));
            frame.setVisible(true);
        });
    }
}
