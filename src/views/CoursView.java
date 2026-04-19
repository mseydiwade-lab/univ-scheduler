package views;

import dao.CoursDAO;
import dao.UtilisateurDAO;
import models.Cours;
import models.Utilisateur;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class CoursView extends VueBase {

    private CoursDAO coursDAO;
    private UtilisateurDAO utilisateurDAO;

    public CoursView(Utilisateur utilisateur) {
        super(utilisateur);
        this.coursDAO = new CoursDAO();
        this.utilisateurDAO = new UtilisateurDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("📚", "Gestion des Cours"), java.awt.BorderLayout.NORTH);
        tableau = creerTableau(new String[]{"ID", "Code", "Intitulé", "Enseignant", "Département", "Crédits", "H/Semaine"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Nouveau cours");
        JButton btnModifier = creerBoutonPrimaire("✏️  Modifier");
        JButton btnSupprimer = creerBoutonDanger("🗑️  Supprimer");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");

        btnAjouter.addActionListener(e -> ajouterCours());
        btnModifier.addActionListener(e -> modifierCours());
        btnSupprimer.addActionListener(e -> supprimerCours());
        btnActualiser.addActionListener(e -> chargerDonnees());

        if (!estAdminOuGestionnaire()) { btnAjouter.setVisible(false); btnModifier.setVisible(false); btnSupprimer.setVisible(false); }

        add(creerBarreBoutons(btnAjouter, btnModifier, btnSupprimer, btnActualiser), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        List<Cours> cours;
        if (utilisateurConnecte.getRole().equals("ENSEIGNANT")) {
            cours = coursDAO.getTousLesCours().stream()
                    .filter(c -> c.getEnseignantId() == utilisateurConnecte.getId())
                    .collect(java.util.stream.Collectors.toList());
        } else if (utilisateurConnecte.getRole().equals("ETUDIANT")) {
            cours = coursDAO.getTousLesCours().stream()
                    .filter(c -> c.getDepartement() != null &&
                            c.getDepartement().equals(utilisateurConnecte.getDepartement()))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            cours = coursDAO.getTousLesCours();
        }
        for (Cours c : cours) {
            String ens = c.getEnseignant() != null ? c.getEnseignant().getPrenom() + " " + c.getEnseignant().getNom() : "N/A";
            modeleTableau.addRow(new Object[]{c.getId(), c.getCode(), c.getIntitule(), ens, c.getDepartement(), c.getCredits(), c.getHeuresParSemaine()});
        }
    }

    private void ajouterCours() {
        List<Utilisateur> enseignants = utilisateurDAO.getTousLesUtilisateurs().stream()
            .filter(u -> u.getRole().equals("ENSEIGNANT")).collect(Collectors.toList());
        if (enseignants.isEmpty()) { JOptionPane.showMessageDialog(this, "Ajoutez d'abord un enseignant !"); return; }
        String[] noms = enseignants.stream().map(u -> u.getPrenom() + " " + u.getNom()).toArray(String[]::new);
        JTextField code = new JTextField(); JTextField intitule = new JTextField();
        JTextField dept = new JTextField(); JTextField credits = new JTextField("3");
        JTextField heures = new JTextField("2"); JComboBox<String> comboEns = new JComboBox<>(noms);
        Object[] champs = {"Code :", code, "Intitulé :", intitule, "Enseignant :", comboEns, "Département :", dept, "Crédits :", credits, "Heures/Semaine :", heures};
        if (JOptionPane.showConfirmDialog(this, champs, "Nouveau cours", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Cours c = new Cours(code.getText(), intitule.getText(), enseignants.get(comboEns.getSelectedIndex()).getId(),
                dept.getText(), Integer.parseInt(credits.getText().isEmpty() ? "3" : credits.getText()),
                Integer.parseInt(heures.getText().isEmpty() ? "2" : heures.getText()));
            if (coursDAO.ajouter(c)) { JOptionPane.showMessageDialog(this, "✅ Cours ajouté !"); chargerDonnees(); }
        }
    }

    private void modifierCours() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un cours !"); return; }
        JTextField intitule = new JTextField(modeleTableau.getValueAt(ligne, 2).toString());
        JTextField dept = new JTextField(modeleTableau.getValueAt(ligne, 4).toString());
        JTextField credits = new JTextField(modeleTableau.getValueAt(ligne, 5).toString());
        JTextField heures = new JTextField(modeleTableau.getValueAt(ligne, 6).toString());
        Object[] champs = {"Intitulé :", intitule, "Département :", dept, "Crédits :", credits, "Heures/Semaine :", heures};
        if (JOptionPane.showConfirmDialog(this, champs, "Modifier cours", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Cours c = new Cours(); c.setId((int) modeleTableau.getValueAt(ligne, 0));
            c.setIntitule(intitule.getText()); c.setDepartement(dept.getText());
            c.setCredits(Integer.parseInt(credits.getText())); c.setHeuresParSemaine(Integer.parseInt(heures.getText()));
            if (coursDAO.modifier(c)) { JOptionPane.showMessageDialog(this, "✅ Cours modifié !"); chargerDonnees(); }
        }
    }

    private void supprimerCours() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un cours !"); return; }
        if (JOptionPane.showConfirmDialog(this, "Supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            if (coursDAO.supprimer((int) modeleTableau.getValueAt(ligne, 0))) { JOptionPane.showMessageDialog(this, "✅ Cours supprimé !"); chargerDonnees(); }
    }
}
