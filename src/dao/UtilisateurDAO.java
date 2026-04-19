package dao;

import database.DatabaseConnection;
import models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public List<Utilisateur> getTousLesUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE est_actif = 1";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) liste.add(extraireUtilisateur(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public Utilisateur connexion(String nomUtilisateur, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ? AND est_actif = 1";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, nomUtilisateur);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extraireUtilisateur(rs);
        } catch (SQLException e) { System.out.println("Erreur connexion : " + e.getMessage()); }
        return null;
    }

    public boolean ajouter(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, email, prenom, nom, role, departement) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, u.getNomUtilisateur());
            ps.setString(2, u.getMotDePasse());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPrenom());
            ps.setString(5, u.getNom());
            ps.setString(6, u.getRole());
            ps.setString(7, u.getDepartement());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur ajout : " + e.getMessage()); return false; }
    }

    public boolean supprimer(int id) {
        String sql = "UPDATE utilisateurs SET est_actif = 0 WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur suppression : " + e.getMessage()); return false; }
    }

    public int compter() {
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM utilisateurs WHERE est_actif = 1");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    private Utilisateur extraireUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNomUtilisateur(rs.getString("nom_utilisateur"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setEmail(rs.getString("email"));
        u.setPrenom(rs.getString("prenom"));
        u.setNom(rs.getString("nom"));
        u.setRole(rs.getString("role"));
        u.setDepartement(rs.getString("departement"));
        u.setGroupeClasse(rs.getString("groupe_classe"));
        u.setEstActif(rs.getInt("est_actif") == 1);
        return u;
    }

    public boolean modifier(int id, String nomUtilisateur, String prenom, String nom,
                            String email, String role, String departement, String motDePasse) {
        String sql = motDePasse != null ?
                "UPDATE utilisateurs SET nom_utilisateur=?, prenom=?, nom=?, email=?, role=?, departement=?, mot_de_passe=? WHERE id=?" :
                "UPDATE utilisateurs SET nom_utilisateur=?, prenom=?, nom=?, email=?, role=?, departement=? WHERE id=?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, nomUtilisateur);
            ps.setString(2, prenom);
            ps.setString(3, nom);
            ps.setString(4, email);
            ps.setString(5, role);
            ps.setString(6, departement);
            if (motDePasse != null) { ps.setString(7, motDePasse); ps.setInt(8, id); }
            else { ps.setInt(7, id); }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur modification : " + e.getMessage()); return false; }
    }
}
