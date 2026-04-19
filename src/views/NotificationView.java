package views;

import dao.NotificationDAO;
import models.Notification;
import models.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationView extends VueBase {

    private NotificationDAO notifDAO;

    public NotificationView(Utilisateur utilisateur) {
        super(utilisateur);
        this.notifDAO = new NotificationDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("🔔", "Notifications"), BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Theme.FOND_CONTENU);
        contenu.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        List<Notification> notifs = notifDAO.getNotificationsParUtilisateur(utilisateurConnecte.getId());

        if (notifs.isEmpty()) {
            JLabel vide = new JLabel("🎉 Aucune notification pour le moment.");
            vide.setFont(Theme.FONT_NORMAL);
            vide.setForeground(Theme.TEXTE_GRIS);
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            contenu.add(vide);
        } else {
            for (Notification n : notifs) {
                contenu.add(creerCarteNotification(n));
                contenu.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(contenu);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.FOND_CONTENU);
        add(scroll, BorderLayout.CENTER);

        JButton btnMarquerLu = creerBoutonPrimaire("✅  Tout marquer comme lu");
        btnMarquerLu.addActionListener(e -> {
            notifDAO.marquerToutLu(utilisateurConnecte.getId());
            JOptionPane.showMessageDialog(this, "✅ Toutes les notifications ont été lues !");
            initialiserInterface();
        });
        add(creerBarreBoutons(btnMarquerLu), BorderLayout.SOUTH);
    }

    private JPanel creerCarteNotification(Notification n) {
        Color couleur = switch (n.getType() != null ? n.getType() : "INFO") {
            case "SUCCES" -> Theme.SUCCES;
            case "AVERTISSEMENT" -> Theme.AVERTISSEMENT;
            case "ERREUR" -> Theme.DANGER;
            default -> Theme.PRIMAIRE;
        };

        JPanel carte = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(n.isEstLu() ? Color.WHITE : new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue(), 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            }
        };
        carte.setOpaque(false);
        carte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 15));
        carte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        carte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textes = new JPanel(new GridLayout(2, 1));
        textes.setOpaque(false);
        JLabel lblTitre = new JLabel(n.getTitre());
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitre.setForeground(n.isEstLu() ? Theme.TEXTE_GRIS : Theme.FOND_SOMBRE);
        JLabel lblMsg = new JLabel(n.getMessage());
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setForeground(Theme.TEXTE_GRIS);
        textes.add(lblTitre);
        textes.add(lblMsg);
        carte.add(textes, BorderLayout.CENTER);

        if (!n.isEstLu()) {
            JPanel badge = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(couleur);
                    g2.fillOval(0, 0, 10, 10);
                }
            };
            badge.setPreferredSize(new Dimension(10, 10));
            badge.setOpaque(false);
            carte.add(badge, BorderLayout.EAST);
        }

        JLabel date = new JLabel(n.getCreeLe() != null ? n.getCreeLe().substring(0, 10) : "");
        date.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        date.setForeground(Theme.TEXTE_GRIS);
        carte.add(date, BorderLayout.SOUTH);

        return carte;
    }
}
