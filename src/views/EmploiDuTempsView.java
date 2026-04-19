package views;

import dao.*;
import models.*;
import javax.swing.*;
import java.io.*;
import java.util.List;

public class EmploiDuTempsView extends VueBase {

    private EmploiDuTempsDAO emploiDAO;
    private CoursDAO coursDAO;
    private SalleDAO salleDAO;
    private NotificationDAO notifDAO;

    public EmploiDuTempsView(Utilisateur utilisateur) {
        super(utilisateur);
        this.emploiDAO = new EmploiDuTempsDAO();
        this.coursDAO = new CoursDAO();
        this.salleDAO = new SalleDAO();
        this.notifDAO = new NotificationDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("📅", "Emplois du Temps"), java.awt.BorderLayout.NORTH);
        tableau = creerTableau(new String[]{"ID", "Cours", "Salle", "Jour", "Heure", "Durée", "Groupe", "Semestre"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Ajouter");
        JButton btnSupprimer = creerBoutonDanger("🗑️  Supprimer");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");
        JButton btnExportCSV = creerBoutonSecondaire("📊  Export Excel");
        JButton btnExportPDF = creerBoutonSecondaire("📄  Export PDF");

        btnAjouter.addActionListener(e -> ajouterEmploi());
        btnSupprimer.addActionListener(e -> supprimerEmploi());
        btnActualiser.addActionListener(e -> chargerDonnees());
        btnExportCSV.addActionListener(e -> exporterCSV());
        btnExportPDF.addActionListener(e -> exporterPDF());

        if (!estAdminOuGestionnaire()) { btnAjouter.setVisible(false); btnSupprimer.setVisible(false); }

        add(creerBarreBoutons(btnAjouter, btnSupprimer, btnActualiser, btnExportCSV, btnExportPDF), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        String[] jours = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        List<EmploiDuTemps> emplois = emploiDAO.getTousLesEmplois();
        if (utilisateurConnecte.getRole().equals("ENSEIGNANT")) {
            emplois = emplois.stream()
                    .filter(e -> e.getCours() != null &&
                            coursDAO.getTousLesCours().stream()
                                    .anyMatch(c -> c.getId() == e.getCoursId() &&
                                            c.getEnseignantId() == utilisateurConnecte.getId()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (utilisateurConnecte.getRole().equals("ETUDIANT")) {
            emplois = emplois.stream()
                    .filter(e -> e.getGroupeClasse() != null &&
                            e.getGroupeClasse().equals(utilisateurConnecte.getGroupeClasse()))
                    .collect(java.util.stream.Collectors.toList());
        }
        for (EmploiDuTemps e : emplois) {
            modeleTableau.addRow(new Object[]{
                    e.getId(),
                    e.getCours() != null ? e.getCours().getIntitule() : "N/A",
                    e.getSalle() != null ? e.getSalle().getNom() : "N/A",
                    e.getJourSemaine() >= 1 && e.getJourSemaine() <= 7 ? jours[e.getJourSemaine()] : "?",
                    e.getHeureDebut(), e.getDureeMinutes() + " min",
                    e.getGroupeClasse(), "S" + e.getSemestre()
            });
        }
    }

    private void exporterCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("emplois_du_temps.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter fw = new FileWriter(fc.getSelectedFile());
                fw.write(emploiDAO.exporterCSV());
                fw.close();
                JOptionPane.showMessageDialog(this, "✅ Export Excel réussi !\n" + fc.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "❌ Erreur export : " + ex.getMessage());
            }
        }
    }

    private void exporterPDF() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("emplois_du_temps.html"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String[] jours = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
                html.append("<title>Emplois du Temps - UNIV-SCHEDULER</title>");
                html.append("<style>");
                html.append("body { font-family: Arial, sans-serif; margin: 30px; }");
                html.append("h1 { color: #4361ee; text-align: center; }");
                html.append("h3 { color: #666; text-align: center; }");
                html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
                html.append("th { background-color: #4361ee; color: white; padding: 12px; text-align: left; }");
                html.append("td { padding: 10px; border-bottom: 1px solid #ddd; }");
                html.append("tr:nth-child(even) { background-color: #f8f9fa; }");
                html.append("tr:hover { background-color: #e8f4ff; }");
                html.append(".footer { text-align: center; margin-top: 30px; color: #999; font-size: 12px; }");
                html.append("@media print { button { display: none; } }");
                html.append("</style></head><body>");
                html.append("<h1>🎓 UNIV-SCHEDULER</h1>");
                html.append("<h3>Emplois du Temps - Année académique 2025-2026</h3>");
                html.append("<button onclick='window.print()' style='background:#4361ee;color:white;border:none;padding:10px 20px;cursor:pointer;border-radius:5px;margin-bottom:20px;'>🖨️ Imprimer / Sauvegarder en PDF</button>");
                html.append("<table><tr><th>Cours</th><th>Salle</th><th>Jour</th><th>Heure</th><th>Durée</th><th>Groupe</th><th>Semestre</th></tr>");

                for (EmploiDuTemps e : emploiDAO.getTousLesEmplois()) {
                    html.append("<tr>");
                    html.append("<td>").append(e.getCours() != null ? e.getCours().getIntitule() : "N/A").append("</td>");
                    html.append("<td>").append(e.getSalle() != null ? e.getSalle().getNom() : "N/A").append("</td>");
                    html.append("<td>").append(e.getJourSemaine() >= 1 && e.getJourSemaine() <= 7 ? jours[e.getJourSemaine()] : "?").append("</td>");
                    html.append("<td>").append(e.getHeureDebut()).append("</td>");
                    html.append("<td>").append(e.getDureeMinutes()).append(" min</td>");
                    html.append("<td>").append(e.getGroupeClasse()).append("</td>");
                    html.append("<td>S").append(e.getSemestre()).append("</td>");
                    html.append("</tr>");
                }
                html.append("</table>");
                html.append("<div class='footer'>Généré par UNIV-SCHEDULER le " + new java.util.Date() + "</div>");
                html.append("</body></html>");

                FileWriter fw = new FileWriter(fc.getSelectedFile());
                fw.write(html.toString());
                fw.close();
                JOptionPane.showMessageDialog(this, "✅ Export PDF réussi !\nOuvrez le fichier dans votre navigateur puis imprimez en PDF.\n" + fc.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "❌ Erreur export : " + ex.getMessage());
            }
        }
    }

    private void ajouterEmploi() {
        List<Cours> cours = coursDAO.getTousLesCours();
        List<Salle> salles = salleDAO.getToutesLesSalles();
        if (cours.isEmpty() || salles.isEmpty()) { JOptionPane.showMessageDialog(this, "Ajoutez d'abord des cours et des salles !"); return; }

        String[] nomsCours = cours.stream().map(c -> c.getCode() + " - " + c.getIntitule()).toArray(String[]::new);
        String[] nomsSalles = salles.stream().map(Salle::getNom).toArray(String[]::new);

        JComboBox<String> comboCours = new JComboBox<>(nomsCours);
        JComboBox<String> comboSalle = new JComboBox<>(nomsSalles);
        JComboBox<String> comboJour = new JComboBox<>(new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"});
        JTextField heure = new JTextField("08:00");
        JTextField duree = new JTextField("120");
        JTextField groupe = new JTextField("L2-INFO-A");
        JComboBox<String> comboSem = new JComboBox<>(new String[]{"1", "2"});

        Object[] champs = {"Cours :", comboCours, "Salle :", comboSalle, "Jour :", comboJour,
                "Heure début :", heure, "Durée (min) :", duree, "Groupe :", groupe, "Semestre :", comboSem};

        if (JOptionPane.showConfirmDialog(this, champs, "Ajouter emploi du temps", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int jourIdx = comboJour.getSelectedIndex() + 1;
            Salle salleSelectionnee = salles.get(comboSalle.getSelectedIndex());

            if (emploiDAO.verifierConflit(salleSelectionnee.getId(), jourIdx, heure.getText())) {
                JOptionPane.showMessageDialog(this, "⚠️ CONFLIT ! Cette salle est déjà occupée à ce créneau !", "Conflit détecté", JOptionPane.WARNING_MESSAGE);
                notifDAO.ajouter(utilisateurConnecte.getId(), "⚠️ Conflit détecté",
                        "La salle " + salleSelectionnee.getNom() + " est déjà occupée le " + comboJour.getSelectedItem() + " à " + heure.getText(), "AVERTISSEMENT");
                return;
            }

            EmploiDuTemps e = new EmploiDuTemps(
                    cours.get(comboCours.getSelectedIndex()).getId(),
                    salleSelectionnee.getId(), jourIdx, heure.getText(),
                    Integer.parseInt(duree.getText().isEmpty() ? "120" : duree.getText()),
                    groupe.getText(), "2025-2026",
                    Integer.parseInt(comboSem.getSelectedItem().toString()));

            if (emploiDAO.ajouter(e)) {
                JOptionPane.showMessageDialog(this, "✅ Emploi du temps ajouté !");
                notifDAO.ajouter(utilisateurConnecte.getId(), "✅ Emploi ajouté",
                        "Nouveau créneau : " + comboJour.getSelectedItem() + " à " + heure.getText() + " en " + salleSelectionnee.getNom(), "SUCCES");
                chargerDonnees();
            }
        }
    }

    private void supprimerEmploi() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un emploi !"); return; }
        if (JOptionPane.showConfirmDialog(this, "Supprimer cet emploi du temps ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            if (emploiDAO.supprimer((int) modeleTableau.getValueAt(ligne, 0))) { JOptionPane.showMessageDialog(this, "✅ Supprimé !"); chargerDonnees(); }
    }
}