import database.DatabaseConnection;
import views.LoginView;
import javax.swing.*;
import java.sql.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Look and Feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and feel par défaut utilisé");
        }

        initialiserBaseDeDonnees();
        SwingUtilities.invokeLater(LoginView::new);
    }

    private static void initialiserBaseDeDonnees() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.getMetaData().getTables(null, null, "utilisateurs", null);
            if (!rs.next()) {
                System.out.println("Création de la base de données...");
                executerScript(conn, "src\\resources\\sql\\init_database.sql");
                System.out.println("✅ Base de données créée avec succès !");
            } else {
                System.out.println("✅ Base de données déjà existante.");
            }
        } catch (Exception e) {
            System.out.println("Erreur initialisation DB : " + e.getMessage());
        }
    }

    private static void executerScript(Connection conn, String chemin) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        StringBuilder sql = new StringBuilder();
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            ligne = ligne.trim();
            if (ligne.startsWith("--") || ligne.isEmpty()) continue;
            sql.append(ligne).append(" ");
            if (ligne.endsWith(";")) {
                String requete = sql.toString().trim();
                if (!requete.isEmpty()) {
                    try {
                        conn.createStatement().execute(requete.substring(0, requete.length() - 1));
                    } catch (SQLException e) {
                        System.out.println("Avertissement SQL : " + e.getMessage());
                    }
                }
                sql = new StringBuilder();
            }
        }
        reader.close();
    }
}
