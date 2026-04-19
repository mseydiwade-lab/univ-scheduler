package views;

import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public abstract class VueBase extends JPanel {

    protected Utilisateur utilisateurConnecte;
    protected JTable tableau;
    protected DefaultTableModel modeleTableau;

    public VueBase(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        setLayout(new BorderLayout());
        setBackground(Theme.FOND_CONTENU);
    }

    protected JPanel creerEnTete(String icone, String titre) {
        JPanel enTete = new JPanel(new BorderLayout());
        enTete.setBackground(Color.WHITE);
        enTete.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(18, 25, 18, 25)
        ));

        JLabel lblTitre = new JLabel(icone + "  " + titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitre.setForeground(Theme.FOND_SOMBRE);
        enTete.add(lblTitre, BorderLayout.WEST);

        return enTete;
    }

    protected JTable creerTableau(String[] colonnes) {
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(modeleTableau);
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Theme.FOND_SOMBRE);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(Theme.FOND_SOMBRE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setAutoCreateRowSorter(true);
        table.setBorder(BorderFactory.createEmptyBorder());

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(100, 116, 139));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        header.setPreferredSize(new Dimension(header.getWidth(), 44));

        return table;
    }

    protected JScrollPane creerScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    protected JPanel creerBarreBoutons(JButton... boutons) {
        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        barre.setBackground(Color.WHITE);
        barre.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        for (JButton btn : boutons) barre.add(btn);
        return barre;
    }

    protected JButton creerBoutonPrimaire(String texte) {
        JButton btn = new JButton(texte);
        btn.setBackground(Theme.PRIMAIRE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    protected JButton creerBoutonDanger(String texte) {
        JButton btn = creerBoutonPrimaire(texte);
        btn.setBackground(Theme.DANGER);
        return btn;
    }

    protected JButton creerBoutonSucces(String texte) {
        JButton btn = creerBoutonPrimaire(texte);
        btn.setBackground(Theme.SUCCES);
        return btn;
    }

    protected JButton creerBoutonSecondaire(String texte) {
        JButton btn = creerBoutonPrimaire(texte);
        btn.setBackground(new Color(226, 232, 240));
        btn.setForeground(Theme.FOND_SOMBRE);
        return btn;
    }

    protected boolean estAdminOuGestionnaire() {
        return utilisateurConnecte.getRole().equals("ADMIN") ||
               utilisateurConnecte.getRole().equals("GESTIONNAIRE");
    }

    protected boolean estAdmin() {
        return utilisateurConnecte.getRole().equals("ADMIN");
    }
}
