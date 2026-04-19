package views;

import dao.*;
import models.*;
import javax.swing.*;
import java.util.List;

public class ReservationView extends VueBase {

    private ReservationDAO reservationDAO;
    private SalleDAO salleDAO;
    private NotificationDAO notifDAO;

    public ReservationView(Utilisateur utilisateur) {
        super(utilisateur);
        this.reservationDAO = new ReservationDAO();
        this.salleDAO = new SalleDAO();
        this.notifDAO = new NotificationDAO();
        initialiserInterface();
    }

    private void initialiserInterface() {
        add(creerEnTete("📋", "Réservations de Salles"), java.awt.BorderLayout.NORTH);
        tableau = creerTableau(new String[]{"ID", "Titre", "Salle", "Demandeur", "Date", "Début", "Fin", "Statut"});
        add(creerScrollPane(tableau), java.awt.BorderLayout.CENTER);

        JButton btnAjouter = creerBoutonSucces("➕  Nouvelle réservation");
        JButton btnApprouver = creerBoutonPrimaire("✅  Approuver");
        JButton btnRejeter = creerBoutonDanger("❌  Rejeter");
        JButton btnAnnuler = creerBoutonSecondaire("🚫  Annuler");
        JButton btnActualiser = creerBoutonSecondaire("🔄  Actualiser");

        btnAjouter.addActionListener(e -> ajouterReservation());
        btnApprouver.addActionListener(e -> changerStatut("APPROUVE"));
        btnRejeter.addActionListener(e -> changerStatut("REJETE"));
        btnAnnuler.addActionListener(e -> changerStatut("ANNULE"));
        btnActualiser.addActionListener(e -> chargerDonnees());

        // Seul admin/gestionnaire peut approuver/rejeter
        if (!estAdminOuGestionnaire()) {
            btnApprouver.setVisible(false);
            btnRejeter.setVisible(false);
        }

        add(creerBarreBoutons(btnAjouter, btnApprouver, btnRejeter, btnAnnuler, btnActualiser), java.awt.BorderLayout.SOUTH);
        chargerDonnees();
    }

    private void chargerDonnees() {
        modeleTableau.setRowCount(0);
        List<Reservation> reservations;
        // Étudiant et enseignant voient leurs propres réservations
        if (estAdminOuGestionnaire()) {
            reservations = reservationDAO.getToutesLesReservations();
        } else {
            reservations = reservationDAO.getReservationsParUtilisateur(utilisateurConnecte.getId());
        }
        for (Reservation r : reservations) {
            modeleTableau.addRow(new Object[]{
                    r.getId(), r.getTitre(),
                    r.getSalle() != null ? r.getSalle().getNom() : "?",
                    r.getUtilisateur() != null ? r.getUtilisateur().getPrenom() + " " + r.getUtilisateur().getNom() : "?",
                    r.getDateReservation(), r.getHeureDebut(), r.getHeureFin(), r.getStatut()
            });
        }
    }

    private void ajouterReservation() {
        List<Salle> salles = salleDAO.getToutesLesSalles();
        if (salles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune salle disponible !");
            return;
        }

        String[] nomsSalles = salles.stream().map(Salle::getNom).toArray(String[]::new);
        JTextField titre = new JTextField();
        JTextField desc = new JTextField();
        JComboBox<String> comboSalle = new JComboBox<>(nomsSalles);
        JTextField date = new JTextField(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        JTextField heureDebut = new JTextField("08:00");
        JTextField heureFin = new JTextField("10:00");
        JTextField motif = new JTextField();

        Object[] champs = {"Titre :", titre, "Description :", desc, "Salle :", comboSalle,
                "Date (yyyy-MM-dd) :", date, "Heure début :", heureDebut, "Heure fin :", heureFin, "Motif :", motif};

        if (JOptionPane.showConfirmDialog(this, champs, "Nouvelle réservation", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (titre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le titre est obligatoire !");
                return;
            }

            // Vérifier que la date n'est pas dans le passé
            String dateChoisie = date.getText();
            String dateAujourdhui = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            if (dateChoisie.compareTo(dateAujourdhui) < 0) {
                JOptionPane.showMessageDialog(this, "❌ Impossible de réserver une date dans le passé !", "Date invalide", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reservation r = new Reservation();
            r.setSalleId(salles.get(comboSalle.getSelectedIndex()).getId());
            r.setUtilisateurId(utilisateurConnecte.getId());
            r.setTitre(titre.getText());
            r.setDescription(desc.getText());
            r.setDateReservation(date.getText());
            r.setHeureDebut(heureDebut.getText());
            r.setHeureFin(heureFin.getText());
            r.setMotif(motif.getText());

            if (reservationDAO.ajouter(r)) {
                JOptionPane.showMessageDialog(this, "✅ Réservation soumise ! En attente d'approbation.");
                notifDAO.ajouter(utilisateurConnecte.getId(), "📋 Réservation soumise",
                        "Votre réservation \"" + titre.getText() + "\" est en attente d'approbation.", "INFO");
                chargerDonnees();
            }
        }
    }

    private void changerStatut(String statut) {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une réservation !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        String titre = modeleTableau.getValueAt(ligne, 1).toString();
        String heureFin = modeleTableau.getValueAt(ligne, 6).toString();
        String date = modeleTableau.getValueAt(ligne, 4).toString();

        // Récupérer l'ID du demandeur directement
        int utilisateurReservationId = utilisateurConnecte.getId();
        for (models.Reservation r : reservationDAO.getToutesLesReservations()) {
            if (r.getId() == id) {
                utilisateurReservationId = r.getUtilisateurId();
                break;
            }
        }

        if (reservationDAO.changerStatut(id, statut)) {
            JOptionPane.showMessageDialog(this, "✅ Statut mis à jour : " + statut);

            String msgStatut = switch (statut) {
                case "APPROUVE" -> "✅ Votre réservation \"" + titre + "\" a été approuvée !";
                case "REJETE" -> "❌ Votre réservation \"" + titre + "\" a été rejetée.";
                case "ANNULE" -> "🚫 Votre réservation \"" + titre + "\" a été annulée.";
                default -> "Statut mis à jour : " + statut;
            };
            String typeNotif = statut.equals("APPROUVE") ? "SUCCES" : "AVERTISSEMENT";
            notifDAO.ajouter(utilisateurReservationId, "📋 Réservation " + statut.toLowerCase(), msgStatut, typeNotif);

            if (statut.equals("APPROUVE")) {
                notifDAO.ajouter(utilisateurReservationId, "⏰ Rappel de fin de réservation",
                        "Votre réservation \"" + titre + "\" se termine à " + heureFin + " le " + date + ".", "INFO");
            }
            chargerDonnees();
        }
    }
}
