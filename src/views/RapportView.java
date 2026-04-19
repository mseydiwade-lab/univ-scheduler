package views;

import dao.*;
import models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class RapportView extends VueBase {

    private EmploiDuTempsDAO emploiDAO;
    private SalleDAO salleDAO;
    private CoursDAO coursDAO;
    private ReservationDAO reservationDAO;

    public RapportView(Utilisateur utilisateur) {
        super(utilisateur);
        this.emploiDAO = new EmploiDuTempsDAO();
        this.salleDAO = new SalleDAO();
        this.coursDAO = new CoursDAO();
        this.reservationDAO = new ReservationDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("📊", "Rapport d'utilisation"), BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Theme.FOND_CONTENU);
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Stats globales
        contenu.add(creerSection("📈 Statistiques Générales"));
        contenu.add(Box.createVerticalStrut(10));

        JPanel panelStats = new JPanel(new GridLayout(2, 3, 15, 15));
        panelStats.setOpaque(false);
        panelStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        int totalSalles = salleDAO.compter();
        int totalCours = coursDAO.compter();
        int totalSeances = emploiDAO.compter();
        int totalReservations = reservationDAO.compter();
        double tauxOccupation = totalSalles > 0 ? (double) totalSeances / (totalSalles * 5) * 100 : 0;

        panelStats.add(creerCarteRapport("🚪 Total Salles", String.valueOf(totalSalles), Theme.PRIMAIRE));
        panelStats.add(creerCarteRapport("📚 Total Cours", String.valueOf(totalCours), Theme.SUCCES));
        panelStats.add(creerCarteRapport("📅 Total Séances", String.valueOf(totalSeances), new Color(245, 158, 11)));
        panelStats.add(creerCarteRapport("📋 Réservations", String.valueOf(totalReservations), Theme.SECONDAIRE));
        panelStats.add(creerCarteRapport("📊 Taux d'occupation", String.format("%.1f%%", tauxOccupation), Theme.DANGER));
        panelStats.add(creerCarteRapport("⏱️ Heures/semaine", String.valueOf(totalSeances * 2) + "h", new Color(139, 92, 246)));

        contenu.add(panelStats);
        contenu.add(Box.createVerticalStrut(25));

        // Rapport par groupe
        contenu.add(creerSection("👥 Rapport par Groupe"));
        contenu.add(Box.createVerticalStrut(10));

        String[] groupes = {"L2-INFO-A", "L2-PHYS", "L2-CHIM"};
        for (String groupe : groupes) {
            long nbSeances = emploiDAO.getTousLesEmplois().stream()
                    .filter(e -> groupe.equals(e.getGroupeClasse())).count();
            if (nbSeances > 0) {
                contenu.add(creerLigneRapport("👥 " + groupe, nbSeances + " séances/semaine", Theme.PRIMAIRE));
                contenu.add(Box.createVerticalStrut(8));
            }
        }

        contenu.add(Box.createVerticalStrut(25));

        // Rapport salles les plus utilisées
        contenu.add(creerSection("🏆 Salles les plus utilisées"));
        contenu.add(Box.createVerticalStrut(10));

        salleDAO.getToutesLesSalles().forEach(s -> {
            long nb = emploiDAO.getTousLesEmplois().stream()
                    .filter(e -> e.getSalleId() == s.getId()).count();
            if (nb > 0) {
                contenu.add(creerLigneRapport("🚪 " + s.getNom(), nb + " séances/semaine", Theme.SUCCES));
                contenu.add(Box.createVerticalStrut(8));
            }
        });

        JScrollPane scroll = new JScrollPane(contenu);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.FOND_CONTENU);
        add(scroll, BorderLayout.CENTER);
    }

    private JLabel creerSection(String titre) {
        JLabel label = new JLabel(titre);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Theme.FOND_SOMBRE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel creerCarteRapport(String titre, String valeur, Color couleur) {
        JPanel carte = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
            }
        };
        carte.setLayout(new GridLayout(2, 1));
        carte.setOpaque(false);
        carte.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitre.setForeground(Theme.TEXTE_GRIS);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValeur.setForeground(couleur);

        carte.add(lblTitre);
        carte.add(lblValeur);
        return carte;
    }

    private JPanel creerLigneRapport(String titre, String valeur, Color couleur) {
        JPanel ligne = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        ligne.setOpaque(false);
        ligne.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitre.setForeground(Theme.FOND_SOMBRE);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblValeur.setForeground(couleur);

        ligne.add(lblTitre, BorderLayout.WEST);
        ligne.add(lblValeur, BorderLayout.EAST);
        return ligne;
    }
}