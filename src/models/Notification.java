package models;

public class Notification {
    private int id;
    private int utilisateurId;
    private String titre;
    private String message;
    private String type;
    private boolean estLu;
    private String creeLe;

    public Notification() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isEstLu() { return estLu; }
    public void setEstLu(boolean estLu) { this.estLu = estLu; }
    public String getCreeLe() { return creeLe; }
    public void setCreeLe(String creeLe) { this.creeLe = creeLe; }
}
