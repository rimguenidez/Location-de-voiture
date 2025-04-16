import java.sql.*;

public class Main {
    public static void main(String[] args) {
        // Paramètres de connexion à MySQL
        String url = "jdbc:mysql://localhost:3306/bdproject";
        String user = "root"; // remplace si ce n'est pas "root"
        String password = "12chocolate"; // remplace par ton vrai mot de passe

        try {
            // Connexion à la base
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion réussie à la base bdproject");

            // Requête d'insertion
            String sql = "INSERT INTO client(id_client, nom, prenom, email, telephone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Valeurs à insérer (change-les si besoin)
            ps.setInt(1, 10); // id_client unique pour éviter les doublons
            ps.setString(2, "Dupont");
            ps.setString(3, "Alice");
            ps.setString(4, "alice.dupont@gmail.com");
            ps.setString(5, "0601020304");

            int lignes = ps.executeUpdate();
            System.out.println("✅ Client inséré : " + lignes + " ligne(s) ajoutée(s)");

            conn.close();
            System.out.println("🔒 Connexion fermée");

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
    }
}