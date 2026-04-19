package views;

import dao.SalleDAO;
import dao.BatimentDAO;
import models.Salle;
import models.Batiment;
import models.Utilisateur;
import javax.swing.*;
import java.util.List;

public class SalleView extends VueBase {

    private SalleDAO salleDAO;
    private BatimentDAO batimentDAO;

    public SalleView(Utilisateur utilisateur) {
        super(utilisateur);
        this.salleDAO = new SalleDAO();
        this.batimentDAO = new BatimentDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("🚪", "Gestion des Salles"), java.awt.BorderLayout.NORTH);
        tableau = creerTableau(new String[]{"ID", "Bâtiment", "Numéro", "Nom", "Étage", "Capacité", "Type"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Nouvelle salle");
        JButton btnSupprimer = creerBoutonDanger("🗑️  Supprimer");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");
        JButton btnRechercher = creerBoutonPrimaire("🔍  Salle disponible");
        JButton btnEquipements = creerBoutonSecondaire("🔧  Équipements");

        btnAjouter.addActionListener(e -> ajouterSalle());
        btnSupprimer.addActionListener(e -> supprimerSalle());
        btnActualiser.addActionListener(e -> chargerDonnees());
        btnRechercher.addActionListener(e -> rechercherDisponible());
        btnEquipements.addActionListener(e -> voirEquipements());

        if (!estAdminOuGestionnaire()) { btnAjouter.setVisible(false); btnSupprimer.setVisible(false); }

        add(creerBarreBoutons(btnAjouter, btnSupprimer, btnActualiser, btnRechercher, btnEquipements), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        List<Batiment> batiments = batimentDAO.getTousLesBatiments();
        for (Salle s : salleDAO.getToutesLesSalles()) {
            String nomBat = batiments.stream().filter(b -> b.getId() == s.getBatimentId())
                    .map(Batiment::getNom).findFirst().orElse("?");
            modeleTableau.addRow(new Object[]{s.getId(), nomBat, s.getNumeroSalle(), s.getNom(), s.getEtage(), s.getCapacite(), s.getTypeSalle()});
        }
    }

    private void ajouterSalle() {
        List<Batiment> bats = batimentDAO.getTousLesBatiments();
        if (bats.isEmpty()) { JOptionPane.showMessageDialog(this, "Ajoutez d'abord un bâtiment !"); return; }
        String[] nomsBats = bats.stream().map(Batiment::getNom).toArray(String[]::new);
        JComboBox<String> comboBat = new JComboBox<>(nomsBats);
        JTextField num = new JTextField(); JTextField nom = new JTextField();
        JTextField etage = new JTextField("0"); JTextField cap = new JTextField();
        JComboBox<String> type = new JComboBox<>(new String[]{"TD", "TP", "AMPHI", "REUNION"});
        Object[] champs = {"Bâtiment :", comboBat, "Numéro :", num, "Nom :", nom, "Étage :", etage, "Capacité :", cap, "Type :", type};
        if (JOptionPane.showConfirmDialog(this, champs, "Nouvelle salle", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Salle s = new Salle(bats.get(comboBat.getSelectedIndex()).getId(), num.getText(), nom.getText(),
                    Integer.parseInt(etage.getText().isEmpty() ? "0" : etage.getText()),
                    Integer.parseInt(cap.getText().isEmpty() ? "0" : cap.getText()), type.getSelectedItem().toString());
            if (salleDAO.ajouter(s)) { JOptionPane.showMessageDialog(this, "✅ Salle ajoutée !"); chargerDonnees(); }
        }
    }

    private void supprimerSalle() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez une salle !"); return; }
        if (JOptionPane.showConfirmDialog(this, "Supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            if (salleDAO.supprimer((int) modeleTableau.getValueAt(ligne, 0))) { JOptionPane.showMessageDialog(this, "✅ Salle supprimée !"); chargerDonnees(); }
    }

    private void rechercherDisponible() {
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        JComboBox<String> comboJour = new JComboBox<>(jours);
        JTextField heure = new JTextField("08:00");
        Object[] champs = {"Jour :", comboJour, "Heure (HH:MM) :", heure};
        if (JOptionPane.showConfirmDialog(this, champs, "Rechercher salle disponible", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            List<Salle> dispo = salleDAO.getSallesDisponibles(comboJour.getSelectedIndex() + 1, heure.getText(), 120);
            modeleTableau.setRowCount(0);
            List<Batiment> bats = batimentDAO.getTousLesBatiments();
            for (Salle s : dispo) {
                String nomBat = bats.stream().filter(b -> b.getId() == s.getBatimentId()).map(Batiment::getNom).findFirst().orElse("?");
                modeleTableau.addRow(new Object[]{s.getId(), nomBat, s.getNumeroSalle(), s.getNom(), s.getEtage(), s.getCapacite(), s.getTypeSalle()});
            }
            JOptionPane.showMessageDialog(this, "🔍 " + dispo.size() + " salle(s) disponible(s) !");
        }
    }

    private void voirEquipements() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord une salle !");
            return;
        }
        int salleId = (int) modeleTableau.getValueAt(ligne, 0);
        String nomSalle = modeleTableau.getValueAt(ligne, 3).toString();
        List<String> equipements = salleDAO.getEquipementsDeSalle(salleId);

        if (equipements.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun équipement pour cette salle.", "Équipements - " + nomSalle, JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String eq : equipements) {
                sb.append("• ").append(eq).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "🔧 Équipements - " + nomSalle, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}