package dao;

import database.DatabaseConnection;
import models.Batiment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatimentDAO {

    public List<Batiment> getTousLesBatiments() {
        List<Batiment> liste = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT * FROM batiments");
            while (rs.next()) liste.add(extraireBatiment(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public boolean ajouter(Batiment b) {
        String sql = "INSERT INTO batiments (nom, code, localisation, nombre_etages, description) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, b.getNom());
            ps.setString(2, b.getCode());
            ps.setString(3, b.getLocalisation());
            ps.setInt(4, b.getNombreEtages());
            ps.setString(5, b.getDescription());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur ajout : " + e.getMessage()); return false; }
    }

    public boolean modifier(Batiment b) {
        String sql = "UPDATE batiments SET nom = ?, localisation = ?, nombre_etages = ?, description = ? WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setString(1, b.getNom());
            ps.setString(2, b.getLocalisation());
            ps.setInt(3, b.getNombreEtages());
            ps.setString(4, b.getDescription());
            ps.setInt(5, b.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur modification : " + e.getMessage()); return false; }
    }

    public boolean supprimer(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM batiments WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur suppression : " + e.getMessage()); return false; }
    }

    public int compter() {
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM batiments");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    private Batiment extraireBatiment(ResultSet rs) throws SQLException {
        Batiment b = new Batiment();
        b.setId(rs.getInt("id"));
        b.setNom(rs.getString("nom"));
        b.setCode(rs.getString("code"));
        b.setLocalisation(rs.getString("localisation"));
        b.setNombreEtages(rs.getInt("nombre_etages"));
        b.setDescription(rs.getString("description"));
        return b;
    }
}
