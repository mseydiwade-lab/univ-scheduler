package dao;

import database.DatabaseConnection;
import models.EmploiDuTemps;
import models.Cours;
import models.Salle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmploiDuTempsDAO {

    public List<EmploiDuTemps> getTousLesEmplois() {
        List<EmploiDuTemps> liste = new ArrayList<>();
        String sql = "SELECT e.*, c.code, c.intitule, s.nom as nom_salle FROM emplois_du_temps e " +
                     "LEFT JOIN cours c ON e.cours_id = c.id LEFT JOIN salles s ON e.salle_id = s.id";
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
            while (rs.next()) liste.add(extraireEmploi(rs));
        } catch (SQLException e) { System.out.println("Erreur : " + e.getMessage()); }
        return liste;
    }

    public boolean verifierConflit(int salleId, int jourSemaine, String heureDebut) {
        String sql = "SELECT COUNT(*) FROM emplois_du_temps WHERE salle_id = ? AND jour_semaine = ? AND heure_debut = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, salleId);
            ps.setInt(2, jourSemaine);
            ps.setString(3, heureDebut);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { System.out.println("Erreur conflit : " + e.getMessage()); }
        return false;
    }

    public boolean ajouter(EmploiDuTemps e) {
        String sql = "INSERT INTO emplois_du_temps (cours_id, salle_id, jour_semaine, heure_debut, duree_minutes, groupe_classe, annee_academique, semestre) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, e.getCoursId());
            ps.setInt(2, e.getSalleId());
            ps.setInt(3, e.getJourSemaine());
            ps.setString(4, e.getHeureDebut());
            ps.setInt(5, e.getDureeMinutes());
            ps.setString(6, e.getGroupeClasse());
            ps.setString(7, e.getAnneeAcademique());
            ps.setInt(8, e.getSemestre());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) { System.out.println("Erreur ajout : " + ex.getMessage()); return false; }
    }

    public boolean supprimer(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM emplois_du_temps WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { System.out.println("Erreur suppression : " + e.getMessage()); return false; }
    }

    public int compter() {
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM emplois_du_temps");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    private EmploiDuTemps extraireEmploi(ResultSet rs) throws SQLException {
        EmploiDuTemps e = new EmploiDuTemps();
        e.setId(rs.getInt("id"));
        e.setCoursId(rs.getInt("cours_id"));
        e.setSalleId(rs.getInt("salle_id"));
        e.setJourSemaine(rs.getInt("jour_semaine"));
        e.setHeureDebut(rs.getString("heure_debut"));
        e.setDureeMinutes(rs.getInt("duree_minutes"));
        e.setGroupeClasse(rs.getString("groupe_classe"));
        e.setAnneeAcademique(rs.getString("annee_academique"));
        e.setSemestre(rs.getInt("semestre"));
        Cours c = new Cours();
        c.setCode(rs.getString("code"));
        c.setIntitule(rs.getString("intitule"));
        e.setCours(c);
        Salle s = new Salle();
        s.setNom(rs.getString("nom_salle"));
        e.setSalle(s);
        return e;
    }
    public String exporterCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Cours,Salle,Jour,Heure,Duree,Groupe,Semestre\n");
        String[] jours = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (EmploiDuTemps e : getTousLesEmplois()) {
            csv.append(e.getId()).append(",");
            csv.append(e.getCours() != null ? e.getCours().getIntitule() : "N/A").append(",");
            csv.append(e.getSalle() != null ? e.getSalle().getNom() : "N/A").append(",");
            csv.append(e.getJourSemaine() >= 1 && e.getJourSemaine() <= 7 ? jours[e.getJourSemaine()] : "?").append(",");
            csv.append(e.getHeureDebut()).append(",");
            csv.append(e.getDureeMinutes()).append(",");
            csv.append(e.getGroupeClasse()).append(",");
            csv.append("S").append(e.getSemestre()).append("\n");
        }
        return csv.toString();
    }
}
