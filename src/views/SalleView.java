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

        // Équipements avec cases à cocher
        JCheckBox chkProjecteur = new JCheckBox("Vidéoprojecteur");
        JCheckBox chkTableau = new JCheckBox("Tableau interactif");
        JCheckBox chkOrdinateurs = new JCheckBox("Ordinateurs");
        JCheckBox chkClim = new JCheckBox("Climatisation");
        JCheckBox chkAudio = new JCheckBox("Système audio");
        JCheckBox chkMicroscope = new JCheckBox("Microscope");

        JPanel panelEquip = new JPanel(new java.awt.GridLayout(3, 2));
        panelEquip.add(chkProjecteur); panelEquip.add(chkTableau);
        panelEquip.add(chkOrdinateurs); panelEquip.add(chkClim);
        panelEquip.add(chkAudio); panelEquip.add(chkMicroscope);

        Object[] champs = {"Bâtiment :", comboBat, "Numéro :", num, "Nom :", nom,
                "Étage :", etage, "Capacité :", cap, "Type :", type,
                "Équipements :", panelEquip};

        if (JOptionPane.showConfirmDialog(this, champs, "Nouvelle salle", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Salle s = new Salle(bats.get(comboBat.getSelectedIndex()).getId(), num.getText(), nom.getText(),
                    Integer.parseInt(etage.getText().isEmpty() ? "0" : etage.getText()),
                    Integer.parseInt(cap.getText().isEmpty() ? "0" : cap.getText()), type.getSelectedItem().toString());

            if (salleDAO.ajouter(s)) {
                // Récupérer l'ID de la salle ajoutée
                List<Salle> salles = salleDAO.getToutesLesSalles();
                int salleId = salles.get(salles.size() - 1).getId();

                // Ajouter les équipements cochés
                if (chkProjecteur.isSelected()) salleDAO.ajouterEquipement(salleId, 1);
                if (chkTableau.isSelected()) salleDAO.ajouterEquipement(salleId, 2);
                if (chkOrdinateurs.isSelected()) salleDAO.ajouterEquipement(salleId, 3);
                if (chkMicroscope.isSelected()) salleDAO.ajouterEquipement(salleId, 4);
                if (chkClim.isSelected()) salleDAO.ajouterEquipement(salleId, 5);
                if (chkAudio.isSelected()) salleDAO.ajouterEquipement(salleId, 6);

                JOptionPane.showMessageDialog(this, "✅ Salle ajoutée avec ses équipements !");
                chargerDonnees();
            }
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
        JComboBox<String> comboType = new JComboBox<>(new String[]{"Tous", "TD", "TP", "AMPHI", "REUNION"});
        JTextField capaciteMin = new JTextField("0");

        // Filtres équipements
        JCheckBox chkProjecteur = new JCheckBox("Vidéoprojecteur");
        JCheckBox chkTableau = new JCheckBox("Tableau interactif");
        JCheckBox chkOrdinateurs = new JCheckBox("Ordinateurs");
        JCheckBox chkClim = new JCheckBox("Climatisation");
        JCheckBox chkAudio = new JCheckBox("Système audio");

        JPanel panelEquip = new JPanel(new java.awt.GridLayout(3, 2));
        panelEquip.add(chkProjecteur); panelEquip.add(chkTableau);
        panelEquip.add(chkOrdinateurs); panelEquip.add(chkClim);
        panelEquip.add(chkAudio);

        Object[] champs = {"Jour :", comboJour, "Heure (HH:MM) :", heure,
                "Type de salle :", comboType, "Capacité minimale :", capaciteMin,
                "Équipements requis :", panelEquip};

        if (JOptionPane.showConfirmDialog(this, champs, "Rechercher salle disponible", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int jour = comboJour.getSelectedIndex() + 1;
            List<Salle> dispo = salleDAO.getSallesDisponibles(jour, heure.getText(), 120);

            // Filtrer par type
            String typeSelectionne = comboType.getSelectedItem().toString();
            if (!typeSelectionne.equals("Tous")) {
                dispo = dispo.stream()
                        .filter(s -> s.getTypeSalle().equals(typeSelectionne))
                        .collect(java.util.stream.Collectors.toList());
            }

            // Filtrer par capacité
            int capMin = Integer.parseInt(capaciteMin.getText().isEmpty() ? "0" : capaciteMin.getText());
            dispo = dispo.stream()
                    .filter(s -> s.getCapacite() >= capMin)
                    .collect(java.util.stream.Collectors.toList());

            // Filtrer par équipements
            List<Integer> equipsRequis = new java.util.ArrayList<>();
            if (chkProjecteur.isSelected()) equipsRequis.add(1);
            if (chkTableau.isSelected()) equipsRequis.add(2);
            if (chkOrdinateurs.isSelected()) equipsRequis.add(3);
            if (chkClim.isSelected()) equipsRequis.add(5);
            if (chkAudio.isSelected()) equipsRequis.add(6);

            if (!equipsRequis.isEmpty()) {
                dispo = dispo.stream()
                        .filter(s -> {
                            List<String> equips = salleDAO.getEquipementsDeSalle(s.getId());
                            return equipsRequis.stream().allMatch(id -> {
                                String[] noms = {"", "Videoprojecteur", "Tableau interactif", "Ordinateurs", "Microscope", "Climatisation", "Systeme audio"};
                                return equips.stream().anyMatch(e -> e.contains(noms[id]));
                            });
                        })
                        .collect(java.util.stream.Collectors.toList());
            }

            modeleTableau.setRowCount(0);
            List<Batiment> bats = batimentDAO.getTousLesBatiments();
            for (Salle s : dispo) {
                String nomBat = bats.stream().filter(b -> b.getId() == s.getBatimentId())
                        .map(Batiment::getNom).findFirst().orElse("?");
                modeleTableau.addRow(new Object[]{s.getId(), nomBat, s.getNumeroSalle(),
                        s.getNom(), s.getEtage(), s.getCapacite(), s.getTypeSalle()});
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