import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class GererRetourClient extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int idClient;
    private String username;

    public GererRetourClient(CardLayout layout, JPanel container, int idClient, String username) {
        this.idClient = idClient;
        this.username = username;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdproject", "root", "12chocolate");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(700, 400));

        // Panel header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        JLabel welcomeLabel = new JLabel("Bienvenue, " + username + " 👤");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup (sans colonne Retourner)
        String[] columns = {"Immatriculation", "Marque", "Modèle", "Couleur"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton charger = new JButton("🔍 Charger Voitures");
        JButton retourner = new JButton("🔁 Retourner Voiture");
        JButton menu = new JButton("🏠 Menu");

        setButtonStyle(charger, new Color(34, 139, 34));
        setButtonStyle(retourner, new Color(0, 128, 255));
        setButtonStyle(menu, new Color(178, 34, 34));

        buttonPanel.add(charger);
        buttonPanel.add(retourner);
        buttonPanel.add(menu);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        charger.addActionListener(e -> chargerVoituresLouees());

        retourner.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une voiture à retourner.");
                return;
            }

            String immatriculation = (String) tableModel.getValueAt(selectedRow, 0);
            effectuerRetour(immatriculation);
        });

        menu.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new ClientPage(username,idClient).setVisible(true);
        });
    }

    private void setButtonStyle(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180, 40));
    }

    private void chargerVoituresLouees() {
        tableModel.setRowCount(0);

        try {
            String sql = """
                SELECT v.immatriculation, v.marque, v.modele, v.couleur
                FROM location l
                JOIN voiture v ON l.immatriculation = v.immatriculation
                WHERE l.id_client = ? AND l.retour_effectif IS NULL
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("immatriculation"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getString("couleur")
                };
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Aucune voiture louée trouvée !");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des voitures louées.");
        }
    }

    private void effectuerRetour(String immatriculation) {
        try {
            String sql = "SELECT date_fin FROM location WHERE immatriculation = ? AND id_client = ? AND retour_effectif IS NULL";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, immatriculation);
            stmt.setInt(2, idClient);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Location non trouvée !");
                return;
            }

            Date dateFin = rs.getDate("date_fin");
            Date maintenant = new Date();
            long retardJours = (maintenant.getTime() - dateFin.getTime()) / (1000 * 60 * 60 * 24);
            long penalite = Math.max(0, retardJours * 50);

            String update = "UPDATE location SET retour_effectif = ?, penalite = ? WHERE immatriculation = ? AND id_client = ? AND retour_effectif IS NULL";
            PreparedStatement updateStmt = connection.prepareStatement(update);
            updateStmt.setDate(1, new java.sql.Date(maintenant.getTime()));
            updateStmt.setLong(2, penalite);
            updateStmt.setString(3, immatriculation);
            updateStmt.setInt(4, idClient);
            updateStmt.executeUpdate();

            String updateVoiture = "UPDATE voiture SET disponible = TRUE WHERE immatriculation = ?";
            PreparedStatement dispoStmt = connection.prepareStatement(updateVoiture);
            dispoStmt.setString(1, immatriculation);
            dispoStmt.executeUpdate();

            if (penalite > 0) {
                JOptionPane.showMessageDialog(this, "Retour avec retard. Pénalité : " + penalite + " DNT.");
            } else {
                JOptionPane.showMessageDialog(this, "Voiture retournée sans pénalité.");
            }

            chargerVoituresLouees();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du retour.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int idClient = 1;
            String username = "JohnDoe";

            CardLayout layout = new CardLayout();
            JPanel container = new JPanel(layout);

            GererRetourClient retourClient = new GererRetourClient(layout, container, idClient, username);
            container.add(retourClient, "RetourClient");

            JFrame frame = new JFrame("Retour Voiture 🔁");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(container);
            frame.setVisible(true);
        });
    }
}
