package dao;

import database.DatabaseConnection;
import models.Reservation;
import models.Salle;
import models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> getToutesLesReservations() {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT r.*, s.nom as nom_salle, u.prenom, u.nom as nom_user FROM reservations r " +
                     "LEFT JOIN salles s ON r.salle_id = s.id LEFT JOIN utilisateurs u ON r.utilisateur_id = u.id";
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
            while (rs.next()) liste.add(extraireReservation(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public List<Reservation> getReservationsParUtilisateur(int utilisateurId) {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT r.*, s.nom as nom_salle, u.prenom, u.nom as nom_user FROM reservations r " +
                     "LEFT JOIN salles s ON r.salle_id = s.id LEFT JOIN utilisateurs u ON r.utilisateur_id = u.id " +
                     "WHERE r.utilisateur_id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(extraireReservation(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public boolean ajouter(Reservation r) {
        String sql = "INSERT INTO reservations (salle_id, utilisateur_id, titre, description, date_reservation, heure_debut, heure_fin, statut, motif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, r.getSalleId());
            ps.setInt(2, r.getUtilisateurId());
            ps.setString(3, r.getTitre());
            ps.setString(4, r.getDescription());
            ps.setString(5, r.getDateReservation());
            ps.setString(6, r.getHeureDebut());
            ps.setString(7, r.getHeureFin());
            ps.setString(8, "EN_ATTENTE");
            ps.setString(9, r.getMotif());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur ajout : " + e.getMessage()); return false; }
    }

    public boolean changerStatut(int id, String statut) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE reservations SET statut = ? WHERE id = ?");
            ps.setString(1, statut);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur statut : " + e.getMessage()); return false; }
    }

    public boolean supprimer(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM reservations WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur suppression : " + e.getMessage()); return false; }
    }

    public int compter() {
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM reservations");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    private Reservation extraireReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));
        r.setSalleId(rs.getInt("salle_id"));
        r.setUtilisateurId(rs.getInt("utilisateur_id"));
        r.setTitre(rs.getString("titre"));
        r.setDescription(rs.getString("description"));
        r.setDateReservation(rs.getString("date_reservation"));
        r.setHeureDebut(rs.getString("heure_debut"));
        r.setHeureFin(rs.getString("heure_fin"));
        r.setStatut(rs.getString("statut"));
        r.setMotif(rs.getString("motif"));
        Salle s = new Salle();
        s.setNom(rs.getString("nom_salle"));
        r.setSalle(s);
        Utilisateur u = new Utilisateur();
        u.setPrenom(rs.getString("prenom") != null ? rs.getString("prenom") : "");
        u.setNom(rs.getString("nom_user") != null ? rs.getString("nom_user") : "");
        r.setUtilisateur(u);
        return r;
    }
}
