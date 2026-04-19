package views;

import dao.UtilisateurDAO;
import models.Utilisateur;
import utils.PasswordGenerator;
import javax.swing.*;
import java.awt.*;

public class ProfilView extends VueBase {

    private UtilisateurDAO utilisateurDAO;
    private JTextField txtNomUtilisateur;
    private JTextField txtPrenom;
    private JTextField txtNom;
    private JTextField txtEmail;
    private JTextField txtDepartement;
    private JTextField txtGroupeClasse;
    private JPasswordField txtNouveauMdp;
    private JPasswordField txtConfirmerMdp;
    private JLabel lblRole;

    public ProfilView(Utilisateur utilisateur) {
        super(utilisateur);
        this.utilisateurDAO = new UtilisateurDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());
        setBackground(Theme.FOND_CONTENU);

        // En-tête
        add(creerEnTete("👤", "Mon Profil"), BorderLayout.NORTH);

        // Contenu principal
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Theme.FOND_CONTENU);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Carte profil
        JPanel carteProfil = new JPanel();
        carteProfil.setLayout(new BoxLayout(carteProfil, BoxLayout.Y_AXIS));
        carteProfil.setBackground(Color.WHITE);
        carteProfil.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        carteProfil.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        carteProfil.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar et nom
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNomComplet = new JLabel(utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());
        lblNomComplet.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNomComplet.setForeground(Theme.FOND_SOMBRE);
        lblNomComplet.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRole = new JLabel(getRoleAffichage(utilisateurConnecte.getRole()));
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRole.setForeground(Theme.TEXTE_GRIS);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblAvatar);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(lblNomComplet);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblRole);

        carteProfil.add(headerPanel);
        carteProfil.add(Box.createVerticalStrut(30));

        // Formulaire
        JPanel formulaire = new JPanel(new GridLayout(0, 2, 15, 15));
        formulaire.setOpaque(false);
        formulaire.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champs actuels
        txtNomUtilisateur = new JTextField(utilisateurConnecte.getNomUtilisateur());
        txtPrenom = new JTextField(utilisateurConnecte.getPrenom());
        txtNom = new JTextField(utilisateurConnecte.getNom());
        txtEmail = new JTextField(utilisateurConnecte.getEmail());
        txtDepartement = new JTextField(utilisateurConnecte.getDepartement());
        txtGroupeClasse = new JTextField(utilisateurConnecte.getGroupeClasse());
        txtGroupeClasse.setEditable(false); // Groupe classe en lecture seule

        // Champs mot de passe
        txtNouveauMdp = new JPasswordField();
        txtNouveauMdp.setToolTipText("Laissez vide pour ne pas changer");
        txtConfirmerMdp = new JPasswordField();
        txtConfirmerMdp.setToolTipText("Confirmez le nouveau mot de passe");

        // Ajouter les champs
        formulaire.add(creerLabel("Nom d'utilisateur :"));
        formulaire.add(txtNomUtilisateur);
        formulaire.add(creerLabel("Prénom :"));
        formulaire.add(txtPrenom);
        formulaire.add(creerLabel("Nom :"));
        formulaire.add(txtNom);
        formulaire.add(creerLabel("Email :"));
        formulaire.add(txtEmail);
        formulaire.add(creerLabel("Département :"));
        formulaire.add(txtDepartement);
        formulaire.add(creerLabel("Groupe/Classe :"));
        formulaire.add(txtGroupeClasse);
        formulaire.add(creerLabel("Nouveau mot de passe :"));
        formulaire.add(txtNouveauMdp);
        formulaire.add(creerLabel("Confirmer mot de passe :"));
        formulaire.add(txtConfirmerMdp);

        carteProfil.add(formulaire);
        carteProfil.add(Box.createVerticalStrut(25));

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBoutons.setOpaque(false);

        JButton btnSauvegarder = creerBoutonSucces("💾  Sauvegarder");
        JButton btnReinitialiser = creerBoutonSecondaire("🔄  Réinitialiser");
        JButton btnGenererMdp = creerBoutonPrimaire("🔐  Générer mot de passe");

        btnSauvegarder.addActionListener(e -> sauvegarderProfil());
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        btnGenererMdp.addActionListener(e -> genererMotDePasse());

        panelBoutons.add(btnSauvegarder);
        panelBoutons.add(btnReinitialiser);
        panelBoutons.add(btnGenererMdp);

        carteProfil.add(panelBoutons);

        // Info
        JLabel lblInfo = new JLabel("ℹ️ Seuls les administrateurs peuvent modifier le rôle et le groupe/classe.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Theme.TEXTE_GRIS);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        carteProfil.add(Box.createVerticalStrut(15));
        carteProfil.add(lblInfo);

        contenu.add(Box.createVerticalGlue());
        contenu.add(carteProfil);
        contenu.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(contenu);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.FOND_CONTENU);
        add(scroll, BorderLayout.CENTER);
    }

    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Theme.FOND_SOMBRE);
        return label;
    }

    private String getRoleAffichage(String role) {
        return switch (role) {
            case "ADMIN" -> "🔴 Administrateur";
            case "GESTIONNAIRE" -> "🟢 Gestionnaire";
            case "ENSEIGNANT" -> "🔵 Enseignant";
            case "ETUDIANT" -> "🟡 Étudiant";
            default -> role;
        };
    }

    private void sauvegarderProfil() {
        // Vérifier les mots de passe
        String nouveauMdp = new String(txtNouveauMdp.getPassword());
        String confirmerMdp = new String(txtConfirmerMdp.getPassword());

        if (!nouveauMdp.isEmpty()) {
            if (!nouveauMdp.equals(confirmerMdp)) {
                JOptionPane.showMessageDialog(this, "❌ Les mots de passe ne correspondent pas !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nouveauMdp.length() < 6) {
                JOptionPane.showMessageDialog(this, "❌ Le mot de passe doit contenir au moins 6 caractères !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Mettre à jour l'utilisateur
        boolean success = utilisateurDAO.modifier(
            utilisateurConnecte.getId(),
            txtNomUtilisateur.getText(),
            txtPrenom.getText(),
            txtNom.getText(),
            txtEmail.getText(),
            utilisateurConnecte.getRole(), // Role inchangé
            txtDepartement.getText(),
            nouveauMdp.isEmpty() ? null : nouveauMdp
        );

        if (success) {
            // Mettre à jour l'objet utilisateur connecté
            utilisateurConnecte.setNomUtilisateur(txtNomUtilisateur.getText());
            utilisateurConnecte.setPrenom(txtPrenom.getText());
            utilisateurConnecte.setNom(txtNom.getText());
            utilisateurConnecte.setEmail(txtEmail.getText());
            utilisateurConnecte.setDepartement(txtDepartement.getText());
            if (!nouveauMdp.isEmpty()) {
                utilisateurConnecte.setMotDePasse(nouveauMdp);
            }

            JOptionPane.showMessageDialog(this, "✅ Profil mis à jour avec succès !");
            
            // Réinitialiser les champs de mot de passe
            txtNouveauMdp.setText("");
            txtConfirmerMdp.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Erreur lors de la mise à jour du profil !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reinitialiserFormulaire() {
        txtNomUtilisateur.setText(utilisateurConnecte.getNomUtilisateur());
        txtPrenom.setText(utilisateurConnecte.getPrenom());
        txtNom.setText(utilisateurConnecte.getNom());
        txtEmail.setText(utilisateurConnecte.getEmail());
        txtDepartement.setText(utilisateurConnecte.getDepartement());
        txtNouveauMdp.setText("");
        txtConfirmerMdp.setText("");
    }

    private void genererMotDePasse() {
        String mdpGenere = PasswordGenerator.generatePassword();
        txtNouveauMdp.setText(mdpGenere);
        txtConfirmerMdp.setText(mdpGenere);
        
        // Copier dans le presse-papiers
        java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(mdpGenere);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        
        JOptionPane.showMessageDialog(this, 
            "🔐 Mot de passe généré : " + mdpGenere + "\n\n" +
            "Le mot de passe a été copié dans le presse-papiers.\n" +
            "N'oubliez pas de cliquer 'Sauvegarder' pour l'enregistrer.");
    }
}
