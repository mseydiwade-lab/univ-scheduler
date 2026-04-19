package dao;

import database.DatabaseConnection;
import models.Cours;
import models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {

    public List<Cours> getTousLesCours() {
        List<Cours> liste = new ArrayList<>();
        String sql = "SELECT c.*, u.prenom, u.nom as nom_enseignant FROM cours c LEFT JOIN utilisateurs u ON c.enseignant_id = u.id";
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
            while (rs.next()) liste.add(extraireCours(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public boolean ajouter(Cours c) {
        String sql = "INSERT INTO cours (code, intitule, description, enseignant_id, departement, credits, heures_par_semaine) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, c.getCode());
            ps.setString(2, c.getIntitule());
            ps.setString(3, c.getDescription());
            ps.setInt(4, c.getEnseignantId());
            ps.setString(5, c.getDepartement());
            ps.setInt(6, c.getCredits());
            ps.setInt(7, c.getHeuresParSemaine());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur ajout : " + e.getMessage()); return false; }
    }

    public boolean modifier(Cours c) {
        String sql = "UPDATE cours SET intitule = ?, description = ?, enseignant_id = ?, departement = ?, credits = ?, heures_par_semaine = ? WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, c.getIntitule());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getEnseignantId());
            ps.setString(4, c.getDepartement());
            ps.setInt(5, c.getCredits());
            ps.setInt(6, c.getHeuresParSemaine());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur modification : " + e.getMessage()); return false; }
    }

    public boolean supprimer(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM cours WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur suppression : " + e.getMessage()); return false; }
    }

    public int compter() {
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM cours");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    private Cours extraireCours(ResultSet rs) throws SQLException {
        Cours c = new Cours();
        c.setId(rs.getInt("id"));
        c.setCode(rs.getString("code"));
        c.setIntitule(rs.getString("intitule"));
        c.setDescription(rs.getString("description"));
        c.setEnseignantId(rs.getInt("enseignant_id"));
        c.setDepartement(rs.getString("departement"));
        c.setCredits(rs.getInt("credits"));
        c.setHeuresParSemaine(rs.getInt("heures_par_semaine"));
        Utilisateur ens = new Utilisateur();
        ens.setPrenom(rs.getString("prenom") != null ? rs.getString("prenom") : "");
        ens.setNom(rs.getString("nom_enseignant") != null ? rs.getString("nom_enseignant") : "");
        c.setEnseignant(ens);
        return c;
    }
}
