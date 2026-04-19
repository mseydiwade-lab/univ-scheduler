package views;

import dao.UtilisateurDAO;
import models.Utilisateur;
import javax.swing.*;
import java.util.List;

public class UtilisateurView extends VueBase {

    private UtilisateurDAO utilisateurDAO;

    public UtilisateurView(Utilisateur utilisateur) {
        super(utilisateur);
        this.utilisateurDAO = new UtilisateurDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("👥", "Gestion des Utilisateurs"), java.awt.BorderLayout.NORTH);
        tableau = creerTableau(new String[]{"ID", "Nom utilisateur", "Prénom", "Nom", "Email", "Rôle", "Département"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Nouvel utilisateur");
        JButton btnModifier = creerBoutonPrimaire("✏️  Modifier");
        JButton btnSupprimer = creerBoutonDanger("🗑️  Supprimer");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");

        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        btnModifier.addActionListener(e -> modifierUtilisateur());
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        btnActualiser.addActionListener(e -> chargerDonnees());

        add(creerBarreBoutons(btnAjouter, btnModifier, btnSupprimer, btnActualiser), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        for (Utilisateur u : utilisateurDAO.getTousLesUtilisateurs()) {
            modeleTableau.addRow(new Object[]{u.getId(), u.getNomUtilisateur(), u.getPrenom(), u.getNom(), u.getEmail(), u.getRole(), u.getDepartement()});
        }
    }

    private void ajouterUtilisateur() {
        JTextField nomUtil = new JTextField(); JTextField mdp = new JTextField();
        JTextField email = new JTextField(); JTextField prenom = new JTextField();
        JTextField nom = new JTextField();
        JComboBox<String> role = new JComboBox<>(new String[]{"ADMIN", "GESTIONNAIRE", "ENSEIGNANT", "ETUDIANT"});
        JTextField dept = new JTextField();
        JTextField groupeClasse = new JTextField();
        Object[] champs = {"Nom utilisateur :", nomUtil, "Mot de passe :", mdp, "Email :", email,
                "Prénom :", prenom, "Nom :", nom, "Rôle :", role, "Département :", dept, "Groupe classe :", groupeClasse};
        if (JOptionPane.showConfirmDialog(this, champs, "Nouvel utilisateur", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Utilisateur u = new Utilisateur(nomUtil.getText(), mdp.getText(), email.getText(), prenom.getText(), nom.getText(), role.getSelectedItem().toString());
            u.setDepartement(dept.getText());
            u.setGroupeClasse(groupeClasse.getText());
            if (utilisateurDAO.ajouter(u)) { JOptionPane.showMessageDialog(this, "✅ Utilisateur ajouté !"); chargerDonnees(); }
        }
    }

    private void modifierUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur à modifier !"); return; }

        int id = (int) modeleTableau.getValueAt(ligne, 0);
        JTextField nomUtil = new JTextField(modeleTableau.getValueAt(ligne, 1).toString());
        JTextField prenom = new JTextField(modeleTableau.getValueAt(ligne, 2).toString());
        JTextField nom = new JTextField(modeleTableau.getValueAt(ligne, 3).toString());
        JTextField email = new JTextField(modeleTableau.getValueAt(ligne, 4).toString());
        JComboBox<String> role = new JComboBox<>(new String[]{"ADMIN", "GESTIONNAIRE", "ENSEIGNANT", "ETUDIANT"});
        role.setSelectedItem(modeleTableau.getValueAt(ligne, 5).toString());
        JTextField dept = new JTextField(modeleTableau.getValueAt(ligne, 6) != null ? modeleTableau.getValueAt(ligne, 6).toString() : "");
        JTextField mdp = new JTextField();
        mdp.setToolTipText("Laisser vide pour ne pas changer");

        Object[] champs = {
                "Nom utilisateur :", nomUtil,
                "Nouveau mot de passe (vide = inchangé) :", mdp,
                "Email :", email,
                "Prénom :", prenom,
                "Nom :", nom,
                "Rôle :", role,
                "Département :", dept
        };

        if (JOptionPane.showConfirmDialog(this, champs, "Modifier utilisateur", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (utilisateurDAO.modifier(id, nomUtil.getText(), prenom.getText(), nom.getText(),
                    email.getText(), role.getSelectedItem().toString(), dept.getText(),
                    mdp.getText().isEmpty() ? null : mdp.getText())) {
                JOptionPane.showMessageDialog(this, "✅ Utilisateur modifié !");
                chargerDonnees();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la modification !");
            }
        }
    }

    private void supprimerUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur !"); return; }
        if ((int) modeleTableau.getValueAt(ligne, 0) == utilisateurConnecte.getId()) {
            JOptionPane.showMessageDialog(this, "Vous ne pouvez pas vous supprimer vous-même !"); return;
        }
        if (JOptionPane.showConfirmDialog(this, "Supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            if (utilisateurDAO.supprimer((int) modeleTableau.getValueAt(ligne, 0))) { JOptionPane.showMessageDialog(this, "✅ Utilisateur supprimé !"); chargerDonnees(); }
    }
}