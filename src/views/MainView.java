package views;

import dao.*;
import models.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainView extends JFrame {

    private Utilisateur utilisateurConnecte;
    private JPanel panelContenu;
    private JLabel labelNotifBadge;
    private NotificationDAO notificationDAO;
    private JButton boutonActif;

    public MainView(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.notificationDAO = new NotificationDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("UNIV-SCHEDULER");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(creerSidebar(), BorderLayout.WEST);

        JPanel panelDroit = new JPanel(new BorderLayout());
        panelDroit.setBackground(Theme.FOND_CONTENU);
        panelDroit.add(creerTopBar(), BorderLayout.NORTH);

        panelContenu = new JPanel(new BorderLayout());
        panelContenu.setBackground(Theme.FOND_CONTENU);
        panelDroit.add(panelContenu, BorderLayout.CENTER);

        panelPrincipal.add(panelDroit, BorderLayout.CENTER);
        add(panelPrincipal);

        afficherDashboard();
        setVisible(true);
    }

    private JPanel creerSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.FOND_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelLogo.setBackground(Theme.FOND_SOMBRE);
        panelLogo.setMaximumSize(new Dimension(230, 70));
        JLabel logo = new JLabel("🎓 UNIV-SCHEDULER");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logo.setForeground(Color.WHITE);
        panelLogo.add(logo);
        sidebar.add(panelLogo);

        // Profil utilisateur
        JPanel panelProfil = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panelProfil.setBackground(new Color(30, 41, 59));
        panelProfil.setMaximumSize(new Dimension(230, 65));
        panelProfil.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Theme.BORDURE));

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMAIRE);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String initiale = utilisateurConnecte.getPrenom().substring(0, 1).toUpperCase();
                g2.drawString(initiale, (36 - fm.stringWidth(initiale)) / 2, 24);
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setOpaque(false);
        panelProfil.add(avatar);

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);
        JLabel nomLabel = new JLabel(utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nomLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel(utilisateurConnecte.getRole());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        roleLabel.setForeground(Theme.SECONDAIRE);
        infos.add(nomLabel);
        infos.add(roleLabel);
        panelProfil.add(infos);
        sidebar.add(panelProfil);

        // Menu items
        sidebar.add(Box.createVerticalStrut(10));
        ajouterSeparateur(sidebar, "MENU PRINCIPAL");

        JButton btnDashboard = creerBoutonMenu("🏠", "Dashboard", () -> afficherDashboard());
        sidebar.add(btnDashboard);
        boutonActif = btnDashboard;
        setActif(btnDashboard);

        ajouterBoutonMenu(sidebar, "🏛️", "Bâtiments", () -> afficherVue(new BatimentView(utilisateurConnecte)));
        ajouterBoutonMenu(sidebar, "🚪", "Salles", () -> afficherVue(new SalleView(utilisateurConnecte)));
        ajouterBoutonMenu(sidebar, "📚", "Cours", () -> afficherVue(new CoursView(utilisateurConnecte)));
        ajouterBoutonMenu(sidebar, "📅", "Emplois du Temps", () -> afficherVue(new EmploiDuTempsView(utilisateurConnecte)));

        sidebar.add(Box.createVerticalStrut(10));
        ajouterSeparateur(sidebar, "SERVICES");

        ajouterBoutonMenu(sidebar, "📋", "Réservations", () -> afficherVue(new ReservationView(utilisateurConnecte)));
        ajouterBoutonMenu(sidebar, "📊", "Rapports", () -> afficherVue(new RapportView(utilisateurConnecte)));

        // Notifications avec badge
        JPanel panelNotif = new JPanel(new BorderLayout());
        panelNotif.setOpaque(false);
        panelNotif.setMaximumSize(new Dimension(230, 44));
        JButton btnNotif = creerBoutonMenu("🔔", "Notifications", () -> afficherVue(new NotificationView(utilisateurConnecte)));
        panelNotif.add(btnNotif, BorderLayout.CENTER);

        int nbNonLues = notificationDAO.compterNonLues(utilisateurConnecte.getId());
        if (nbNonLues > 0) {
            labelNotifBadge = new JLabel(String.valueOf(nbNonLues));
            labelNotifBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            labelNotifBadge.setForeground(Color.WHITE);
            labelNotifBadge.setBackground(Theme.DANGER);
            labelNotifBadge.setOpaque(true);
            labelNotifBadge.setHorizontalAlignment(SwingConstants.CENTER);
            labelNotifBadge.setPreferredSize(new Dimension(20, 20));
            panelNotif.add(labelNotifBadge, BorderLayout.EAST);
        }
        sidebar.add(panelNotif);

        if (utilisateurConnecte.getRole().equals("ADMIN")) {
            sidebar.add(Box.createVerticalStrut(10));
            ajouterSeparateur(sidebar, "ADMINISTRATION");
            ajouterBoutonMenu(sidebar, "👥", "Utilisateurs", () -> afficherVue(new UtilisateurView(utilisateurConnecte)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Déconnexion
        JButton btnDeconnexion = creerBoutonMenu("🚪", "Déconnexion", () -> seDeconnecter());
        btnDeconnexion.setForeground(Theme.DANGER);
        sidebar.add(btnDeconnexion);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private void ajouterSeparateur(JPanel panel, String texte) {
        JPanel sep = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(230, 30));
        JLabel label = new JLabel(texte);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(new Color(100, 116, 139));
        sep.add(label);
        panel.add(sep);
    }

    private void ajouterBoutonMenu(JPanel panel, String icone, String texte, Runnable action) {
        JButton btn = creerBoutonMenu(icone, texte, action);
        panel.add(btn);
    }

    private JButton creerBoutonMenu(String icone, String texte, Runnable action) {
        JButton btn = new JButton(icone + "  " + texte) {
            @Override
            protected void paintComponent(Graphics g) {
                if (this == boutonActif) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(67, 97, 238, 30));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    g2.setColor(Theme.PRIMAIRE);
                    g2.fillRoundRect(0, 4, 3, getHeight() - 8, 3, 3);
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Theme.TEXTE_GRIS);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(230, 44));
        btn.setPreferredSize(new Dimension(230, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != boutonActif) btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != boutonActif) btn.setForeground(Theme.TEXTE_GRIS);
            }
        });

        btn.addActionListener(e -> {
            setActif(btn);
            action.run();
        });

        return btn;
    }

    private void setActif(JButton btn) {
        if (boutonActif != null) {
            boutonActif.setForeground(Theme.TEXTE_GRIS);
        }
        boutonActif = btn;
        btn.setForeground(Theme.PRIMAIRE);
        btn.repaint();
    }

    private JPanel creerTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titre = new JLabel("  Tableau de bord");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titre.setForeground(Theme.FOND_SOMBRE);
        topBar.add(titre, BorderLayout.WEST);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        droite.setOpaque(false);
        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE dd MMMM yyyy", java.util.Locale.FRENCH).format(new java.util.Date()));
        dateLabel.setFont(Theme.FONT_PETIT);
        dateLabel.setForeground(Theme.TEXTE_GRIS);
        droite.add(dateLabel);
        topBar.add(droite, BorderLayout.EAST);

        return topBar;
    }

    private void afficherVue(JPanel vue) {
        panelContenu.removeAll();
        panelContenu.add(vue, BorderLayout.CENTER);
        panelContenu.revalidate();
        panelContenu.repaint();
    }

    private void afficherDashboard() {
        afficherVue(new DashboardView(utilisateurConnecte));
    }

    private void seDeconnecter() {
        int rep = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
        if (rep == JOptionPane.YES_OPTION) {
            dispose();
            new LoginView();
        }
    }
}
