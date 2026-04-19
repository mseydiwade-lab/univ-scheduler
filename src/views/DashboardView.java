package views;

import dao.*;
import models.Utilisateur;
import models.EmploiDuTemps;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardView extends JPanel {

    private Utilisateur utilisateur;

    public DashboardView(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        setLayout(new BorderLayout());
        setBackground(Theme.FOND_CONTENU);
        initialiserInterface();
    }

    private void initialiserInterface() {
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Theme.FOND_CONTENU);
        contenu.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Message de bienvenue
        JPanel panelBienvenue = new JPanel(new BorderLayout());
        panelBienvenue.setOpaque(false);
        panelBienvenue.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel bienvenue = new JLabel("Bonjour, " + utilisateur.getPrenom() + " 👋");
        bienvenue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        bienvenue.setForeground(Theme.FOND_SOMBRE);
        panelBienvenue.add(bienvenue, BorderLayout.WEST);

        JLabel role = new JLabel(getRoleLabel());
        role.setFont(Theme.FONT_NORMAL);
        role.setForeground(Theme.TEXTE_GRIS);
        panelBienvenue.add(role, BorderLayout.EAST);
        contenu.add(panelBienvenue);
        contenu.add(Box.createVerticalStrut(20));

        // Cartes statistiques
        JPanel panelCartes = new JPanel(new GridLayout(1, 4, 15, 0));
        panelCartes.setOpaque(false);
        panelCartes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        BatimentDAO batDAO = new BatimentDAO();
        SalleDAO salDAO = new SalleDAO();
        CoursDAO couDAO = new CoursDAO();
        EmploiDuTempsDAO empDAO = new EmploiDuTempsDAO();
        ReservationDAO resDAO = new ReservationDAO();
        UtilisateurDAO utiDAO = new UtilisateurDAO();

        if (utilisateur.getRole().equals("ENSEIGNANT")) {
            long nbCours = couDAO.getTousLesCours().stream()
                    .filter(c -> c.getEnseignantId() == utilisateur.getId()).count();
            long nbSeances = empDAO.getTousLesEmplois().stream()
                    .filter(e -> couDAO.getTousLesCours().stream()
                            .anyMatch(c -> c.getId() == e.getCoursId() && c.getEnseignantId() == utilisateur.getId())).count();
            panelCartes.add(creerCarte("📚", "Mes Cours", String.valueOf(nbCours), new Color(245, 158, 11), "ce semestre"));
            panelCartes.add(creerCarte("📅", "Mes Séances", String.valueOf(nbSeances), Theme.PRIMAIRE, "cette semaine"));
            panelCartes.add(creerCarte("🚪", "Salles", String.valueOf(salDAO.compter()), Theme.SUCCES, "disponibles"));
        } else if (utilisateur.getRole().equals("ETUDIANT")) {
            long nbSeances = empDAO.getTousLesEmplois().stream()
                    .filter(e -> e.getGroupeClasse() != null &&
                            e.getGroupeClasse().equals(utilisateur.getGroupeClasse()))
                    .count();
            panelCartes.add(creerCarte("📅", "Mes Séances", String.valueOf(nbSeances), Theme.PRIMAIRE, "cette semaine"));
            panelCartes.add(creerCarte("🚪", "Salles", String.valueOf(salDAO.compter()), Theme.SUCCES, "disponibles"));
            panelCartes.add(creerCarte("📚", "Cours", String.valueOf(couDAO.compter()), new Color(245, 158, 11), "ce semestre"));
        } else if (utilisateur.getRole().equals("GESTIONNAIRE")) {
        panelCartes.add(creerCarte("🏛️", "Bâtiments", String.valueOf(batDAO.compter()), Theme.PRIMAIRE, "+0 ce mois"));
        panelCartes.add(creerCarte("🚪", "Salles", String.valueOf(salDAO.compter()), Theme.SUCCES, "actives"));
        panelCartes.add(creerCarte("📚", "Cours", String.valueOf(couDAO.compter()), new Color(245, 158, 11), "ce semestre"));
        panelCartes.add(creerCarte("📅", "Séances", String.valueOf(empDAO.compter()), Theme.SECONDAIRE, "planifiées"));
    } else {
        panelCartes.add(creerCarte("🏛️", "Bâtiments", String.valueOf(batDAO.compter()), Theme.PRIMAIRE, "+0 ce mois"));
        panelCartes.add(creerCarte("🚪", "Salles", String.valueOf(salDAO.compter()), Theme.SUCCES, "actives"));
        panelCartes.add(creerCarte("📚", "Cours", String.valueOf(couDAO.compter()), new Color(245, 158, 11), "ce semestre"));
        panelCartes.add(creerCarte("👥", "Utilisateurs", String.valueOf(utiDAO.compter()), new Color(239, 68, 68), "actifs"));
    }

        contenu.add(panelCartes);
        contenu.add(Box.createVerticalStrut(25));

        // Emplois du temps aujourd'hui
        JLabel titreEmplois = new JLabel("📅 Emplois du temps de la semaine");
        titreEmplois.setFont(Theme.FONT_SOUS_TITRE);
        titreEmplois.setForeground(Theme.FOND_SOMBRE);
        titreEmplois.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenu.add(titreEmplois);
        contenu.add(Box.createVerticalStrut(12));

        List<EmploiDuTemps> emplois = empDAO.getTousLesEmplois();
        if (utilisateur.getRole().equals("ENSEIGNANT")) {
            emplois = emplois.stream()
                    .filter(e -> new dao.CoursDAO().getTousLesCours().stream()
                            .anyMatch(c -> c.getId() == e.getCoursId() &&
                                    c.getEnseignantId() == utilisateur.getId()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (utilisateur.getRole().equals("ETUDIANT")) {
            emplois = emplois.stream()
                    .filter(e -> e.getGroupeClasse() != null &&
                            e.getGroupeClasse().equals(utilisateur.getGroupeClasse()))
                    .collect(java.util.stream.Collectors.toList());
        }
        JPanel panelEmplois = new JPanel();
        panelEmplois.setLayout(new BoxLayout(panelEmplois, BoxLayout.Y_AXIS));
        panelEmplois.setOpaque(false);
        panelEmplois.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (emplois.isEmpty()) {
            JLabel vide = new JLabel("Aucun emploi du temps enregistré.");
            vide.setFont(Theme.FONT_NORMAL);
            vide.setForeground(Theme.TEXTE_GRIS);
            panelEmplois.add(vide);
        } else {
            for (EmploiDuTemps e : emplois.subList(0, Math.min(5, emplois.size()))) {
                panelEmplois.add(creerLigneEmploi(e));
                panelEmplois.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(panelEmplois);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenu.add(scroll);

        add(new JScrollPane(contenu) {{
            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(false);
            getViewport().setOpaque(false);
        }}, BorderLayout.CENTER);
    }

    private JPanel creerCarte(String icone, String titre, String valeur, Color couleur, String sous) {
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
        carte.setLayout(new GridBagLayout());
        carte.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblIcone = new JLabel(icone + " " + titre);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblIcone.setForeground(Theme.TEXTE_GRIS);
        carte.add(lblIcone, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 15, 4, 10);
        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValeur.setForeground(couleur);
        carte.add(lblValeur, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 15, 10, 10);
        JLabel lblSous = new JLabel(sous);
        lblSous.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSous.setForeground(Theme.TEXTE_GRIS);
        carte.add(lblSous, gbc);

        return carte;
    }

    private JPanel creerLigneEmploi(EmploiDuTemps e) {
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
        ligne.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        Color[] couleurs = {Theme.PRIMAIRE, Theme.SUCCES, new Color(245, 158, 11), Theme.SECONDAIRE, Theme.DANGER};
        Color couleur = couleurs[e.getJourSemaine() % couleurs.length];

        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        badge.setPreferredSize(new Dimension(80, 36));
        badge.setOpaque(false);
        badge.setLayout(new GridBagLayout());
        JLabel lblJour = new JLabel(e.getJourNom().substring(0, 3).toUpperCase());
        lblJour.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblJour.setForeground(couleur);
        badge.add(lblJour);
        ligne.add(badge, BorderLayout.WEST);

        JPanel milieu = new JPanel(new GridLayout(2, 1));
        milieu.setOpaque(false);
        milieu.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        String intitule = e.getCours() != null ? e.getCours().getIntitule() : "Cours";
        JLabel lblCours = new JLabel(intitule);
        lblCours.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCours.setForeground(Theme.FOND_SOMBRE);

        String salleNom = e.getSalle() != null ? e.getSalle().getNom() : "Salle";
        JLabel lblSalle = new JLabel(salleNom + " • " + e.getGroupeClasse());
        lblSalle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSalle.setForeground(Theme.TEXTE_GRIS);

        milieu.add(lblCours);
        milieu.add(lblSalle);
        ligne.add(milieu, BorderLayout.CENTER);

        JLabel lblHeure = new JLabel(e.getHeureDebut() + " • " + e.getDureeMinutes() + "min");
        lblHeure.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHeure.setForeground(Theme.TEXTE_GRIS);
        ligne.add(lblHeure, BorderLayout.EAST);

        return ligne;
    }

    private String getRoleLabel() {
        return switch (utilisateur.getRole()) {
            case "ADMIN" -> "Administrateur système";
            case "GESTIONNAIRE" -> "Gestionnaire d'emplois du temps";
            case "ENSEIGNANT" -> "Enseignant";
            case "ETUDIANT" -> "Étudiant";
            default -> utilisateur.getRole();
        };
    }
}
