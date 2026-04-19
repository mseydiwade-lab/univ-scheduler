package models;

public class Reservation {
    private int id;
    private int salleId;
    private int utilisateurId;
    private String titre;
    private String description;
    private String dateReservation;
    private String heureDebut;
    private String heureFin;
    private String statut;
    private String motif;
    private Salle salle;
    private Utilisateur utilisateur;

    public Reservation() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSalleId() { return salleId; }
    public void setSalleId(int salleId) { this.salleId = salleId; }
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDateReservation() { return dateReservation; }
    public void setDateReservation(String dateReservation) { this.dateReservation = dateReservation; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    @Override
    public String toString() { return titre + " - " + dateReservation; }
}
