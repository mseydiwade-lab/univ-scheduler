package views;

import dao.BatimentDAO;
import models.Batiment;
import models.Utilisateur;
import javax.swing.*;
import java.util.List;

public class BatimentView extends VueBase {

    private BatimentDAO batimentDAO;

    public BatimentView(Utilisateur utilisateur) {
        super(utilisateur);
        this.batimentDAO = new BatimentDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("🏛️", "Gestion des Bâtiments"), java.awt.BorderLayout.NORTH);

        tableau = creerTableau(new String[]{"ID", "Nom", "Code", "Localisation", "Étages", "Description"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Nouveau bâtiment");
        JButton btnModifier = creerBoutonPrimaire("✏️  Modifier");
        JButton btnSupprimer = creerBoutonDanger("🗑️  Supprimer");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");

        btnAjouter.addActionListener(e -> ajouterBatiment());
        btnModifier.addActionListener(e -> modifierBatiment());
        btnSupprimer.addActionListener(e -> supprimerBatiment());
        btnActualiser.addActionListener(e -> chargerDonnees());

        if (!estAdminOuGestionnaire()) {
            btnAjouter.setVisible(false);
            btnModifier.setVisible(false);
            btnSupprimer.setVisible(false);
        }

        add(creerBarreBoutons(btnAjouter, btnModifier, btnSupprimer, btnActualiser), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        for (Batiment b : batimentDAO.getTousLesBatiments()) {
            modeleTableau.addRow(new Object[]{b.getId(), b.getNom(), b.getCode(), b.getLocalisation(), b.getNombreEtages(), b.getDescription()});
        }
    }

    private void ajouterBatiment() {
        JTextField nom = new JTextField(); JTextField code = new JTextField();
        JTextField loc = new JTextField(); JTextField etages = new JTextField("1");
        JTextField desc = new JTextField();
        Object[] champs = {"Nom :", nom, "Code :", code, "Localisation :", loc, "Étages :", etages, "Description :", desc};
        if (JOptionPane.showConfirmDialog(this, champs, "Nouveau bâtiment", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Batiment b = new Batiment(nom.getText(), code.getText(), loc.getText(), Integer.parseInt(etages.getText().isEmpty() ? "1" : etages.getText()));
            b.setDescription(desc.getText());
            if (batimentDAO.ajouter(b)) { JOptionPane.showMessageDialog(this, "✅ Bâtiment ajouté !"); chargerDonnees(); }
        }
    }

    private void modifierBatiment() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un bâtiment !"); return; }
        JTextField nom = new JTextField(modeleTableau.getValueAt(ligne, 1).toString());
        JTextField loc = new JTextField(modeleTableau.getValueAt(ligne, 3).toString());
        JTextField etages = new JTextField(modeleTableau.getValueAt(ligne, 4).toString());
        Object[] champs = {"Nom :", nom, "Localisation :", loc, "Étages :", etages};
        if (JOptionPane.showConfirmDialog(this, champs, "Modifier", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Batiment b = new Batiment(); b.setId((int) modeleTableau.getValueAt(ligne, 0));
            b.setNom(nom.getText()); b.setLocalisation(loc.getText());
            b.setNombreEtages(Integer.parseInt(etages.getText()));
            if (batimentDAO.modifier(b)) { JOptionPane.showMessageDialog(this, "✅ Bâtiment modifié !"); chargerDonnees(); }
        }
    }

    private void supprimerBatiment() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un bâtiment !"); return; }
        if (JOptionPane.showConfirmDialog(this, "Supprimer ce bâtiment ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (batimentDAO.supprimer((int) modeleTableau.getValueAt(ligne, 0))) { JOptionPane.showMessageDialog(this, "✅ Bâtiment supprimé !"); chargerDonnees(); }
        }
    }
}
