package views;

import dao.UtilisateurDAO;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginView extends JFrame {

    private JTextField champNomUtilisateur;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private UtilisateurDAO utilisateurDAO;

    public LoginView() {
        utilisateurDAO = new UtilisateurDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("UNIV-SCHEDULER - Connexion");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));

        // Panel gauche - Décoratif
        JPanel panelGauche = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMAIRE, getWidth(), getHeight(), new Color(114, 9, 183));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Cercles décoratifs
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillOval(-50, -50, 300, 300);
                g2d.fillOval(getWidth() - 100, getHeight() - 100, 250, 250);
                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillOval(50, getHeight() / 2, 200, 200);
            }
        };
        panelGauche.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel icone = new JLabel("🎓");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        panelGauche.add(icone, gbc);

        gbc.gridy = 1;
        JLabel titre = new JLabel("UNIV-SCHEDULER");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titre.setForeground(Color.WHITE);
        panelGauche.add(titre, gbc);

        gbc.gridy = 2;
        JLabel sousTitre = new JLabel("Gestion Intelligente des Salles");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sousTitre.setForeground(new Color(255, 255, 255, 180));
        panelGauche.add(sousTitre, gbc);

        gbc.gridy = 3;
        JLabel sousTitre2 = new JLabel("et des Emplois du Temps");
        sousTitre2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sousTitre2.setForeground(new Color(255, 255, 255, 180));
        panelGauche.add(sousTitre2, gbc);

        // Panel droit - Formulaire
        JPanel panelDroit = new JPanel(new GridBagLayout());
        panelDroit.setBackground(Color.WHITE);
        GridBagConstraints gbcD = new GridBagConstraints();
        gbcD.insets = new Insets(8, 30, 8, 30);
        gbcD.fill = GridBagConstraints.HORIZONTAL;
        gbcD.gridx = 0; gbcD.gridy = 0;

        JLabel titreConnexion = new JLabel("Connexion");
        titreConnexion.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titreConnexion.setForeground(Theme.FOND_SOMBRE);
        panelDroit.add(titreConnexion, gbcD);

        gbcD.gridy = 1;
        JLabel sousConnexion = new JLabel("Entrez vos identifiants pour continuer");
        sousConnexion.setFont(Theme.FONT_PETIT);
        sousConnexion.setForeground(Theme.TEXTE_GRIS);
        panelDroit.add(sousConnexion, gbcD);

        gbcD.gridy = 2;
        gbcD.insets = new Insets(20, 30, 4, 30);
        JLabel labelNom = new JLabel("Nom d'utilisateur");
        labelNom.setFont(Theme.FONT_BOLD);
        labelNom.setForeground(Theme.FOND_SOMBRE);
        panelDroit.add(labelNom, gbcD);

        gbcD.gridy = 3;
        gbcD.insets = new Insets(0, 30, 8, 30);
        champNomUtilisateur = new JTextField();
        champNomUtilisateur.setFont(Theme.FONT_NORMAL);
        champNomUtilisateur.setPreferredSize(new Dimension(300, 42));
        champNomUtilisateur.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(203, 213, 225), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelDroit.add(champNomUtilisateur, gbcD);

        gbcD.gridy = 4;
        gbcD.insets = new Insets(8, 30, 4, 30);
        JLabel labelMdp = new JLabel("Mot de passe");
        labelMdp.setFont(Theme.FONT_BOLD);
        labelMdp.setForeground(Theme.FOND_SOMBRE);
        panelDroit.add(labelMdp, gbcD);

        gbcD.gridy = 5;
        gbcD.insets = new Insets(0, 30, 20, 30);
        champMotDePasse = new JPasswordField();
        champMotDePasse.setFont(Theme.FONT_NORMAL);
        champMotDePasse.setPreferredSize(new Dimension(300, 42));
        champMotDePasse.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(203, 213, 225), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelDroit.add(champMotDePasse, gbcD);

        gbcD.gridy = 6;
        gbcD.insets = new Insets(0, 30, 8, 30);
        boutonConnexion = new JButton("Se connecter") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMAIRE, getWidth(), 0, new Color(114, 9, 183));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        boutonConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boutonConnexion.setForeground(Color.WHITE);
        boutonConnexion.setContentAreaFilled(false);
        boutonConnexion.setBorderPainted(false);
        boutonConnexion.setPreferredSize(new Dimension(300, 44));
        boutonConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonConnexion.addActionListener(e -> seConnecter());
        champMotDePasse.addActionListener(e -> seConnecter());
        panelDroit.add(boutonConnexion, gbcD);

        gbcD.gridy = 7;
        gbcD.insets = new Insets(10, 30, 8, 30);
        JLabel infoLabel = new JLabel("Compte test: admin / password123");
        infoLabel.setFont(Theme.FONT_PETIT);
        infoLabel.setForeground(Theme.TEXTE_GRIS);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelDroit.add(infoLabel, gbcD);

        panelPrincipal.add(panelGauche);
        panelPrincipal.add(panelDroit);
        add(panelPrincipal);
        setVisible(true);
    }

    private void seConnecter() {
        String nomUtilisateur = champNomUtilisateur.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword()).trim();

        if (nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Utilisateur utilisateur = utilisateurDAO.connexion(nomUtilisateur, motDePasse);

        if (utilisateur != null) {
            dispose();
            new MainView(utilisateur);
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects !", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            champMotDePasse.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}
