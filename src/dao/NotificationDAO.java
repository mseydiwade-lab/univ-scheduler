package dao;

import database.DatabaseConnection;
import models.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> getNotificationsParUtilisateur(int utilisateurId) {
        List<Notification> liste = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE utilisateur_id = ? ORDER BY cree_le DESC";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(extraireNotification(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public int compterNonLues(int utilisateurId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE utilisateur_id = ? AND est_lu = 0";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public boolean ajouter(int utilisateurId, String titre, String message, String type) {
        String sql = "INSERT INTO notifications (utilisateur_id, titre, message, type) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            ps.setString(2, titre);
            ps.setString(3, message);
            ps.setString(4, type);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur ajout : " + e.getMessage()); return false; }
    }

    public boolean marquerToutLu(int utilisateurId) {
        String sql = "UPDATE notifications SET est_lu = 1 WHERE utilisateur_id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    private Notification extraireNotification(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setId(rs.getInt("id"));
        n.setUtilisateurId(rs.getInt("utilisateur_id"));
        n.setTitre(rs.getString("titre"));
        n.setMessage(rs.getString("message"));
        n.setType(rs.getString("type"));
        n.setEstLu(rs.getInt("est_lu") == 1);
        n.setCreeLe(rs.getString("cree_le"));
        return n;
    }
}
