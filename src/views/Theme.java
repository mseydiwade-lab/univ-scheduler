package views;
import javax.swing.*;
import java.awt.*;

public class Theme {
    // Couleurs principales
    public static final Color PRIMAIRE = new Color(67, 97, 238);
    public static final Color SECONDAIRE = new Color(76, 201, 240);
    public static final Color SUCCES = new Color(72, 199, 142);
    public static final Color DANGER = new Color(255, 90, 101);
    public static final Color AVERTISSEMENT = new Color(255, 193, 7);
    public static final Color INFO = new Color(32, 201, 151);

    // Fond
    public static final Color FOND_SOMBRE = new Color(15, 23, 42);
    public static final Color FOND_SIDEBAR = new Color(23, 32, 56);
    public static final Color FOND_CARTE = new Color(30, 41, 59);
    public static final Color FOND_CLAIR = new Color(248, 250, 252);
    public static final Color FOND_CONTENU = new Color(241, 245, 249);

    // Texte
    public static final Color TEXTE_BLANC = new Color(248, 250, 252);
    public static final Color TEXTE_GRIS = new Color(148, 163, 184);
    public static final Color TEXTE_SOMBRE = new Color(15, 23, 42);

    // Bordures
    public static final Color BORDURE = new Color(51, 65, 85);

    // Fonts
    public static final Font FONT_TITRE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SOUS_TITRE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_PETIT = new Font("Segoe UI", Font.PLAIN, 11);

    public static JButton creerBouton(String texte, Color couleur) {
        javax.swing.JButton btn = new javax.swing.JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    public static javax.swing.JLabel creerLabel(String texte, Font font, Color couleur) {
        javax.swing.JLabel label = new javax.swing.JLabel(texte);
        label.setFont(font);
        label.setForeground(couleur);
        return label;
    }
}
