package models;

public class Salle {
    private int id;
    private int batimentId;
    private String numeroSalle;
    private String nom;
    private int etage;
    private int capacite;
    private String typeSalle;
    private boolean estActive;
    private Batiment batiment;

    public Salle() {}

    public Salle(int batimentId, String numeroSalle, String nom,
                 int etage, int capacite, String typeSalle) {
        this.batimentId = batimentId;
        this.numeroSalle = numeroSalle;
        this.nom = nom;
        this.etage = etage;
        this.capacite = capacite;
        this.typeSalle = typeSalle;
        this.estActive = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBatimentId() { return batimentId; }
    public void setBatimentId(int batimentId) { this.batimentId = batimentId; }
    public String getNumeroSalle() { return numeroSalle; }
    public void setNumeroSalle(String numeroSalle) { this.numeroSalle = numeroSalle; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getEtage() { return etage; }
    public void setEtage(int etage) { this.etage = etage; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getTypeSalle() { return typeSalle; }
    public void setTypeSalle(String typeSalle) { this.typeSalle = typeSalle; }
    public boolean isEstActive() { return estActive; }
    public void setEstActive(boolean estActive) { this.estActive = estActive; }
    public Batiment getBatiment() { return batiment; }
    public void setBatiment(Batiment batiment) { this.batiment = batiment; }

    @Override
    public String toString() { return nom + " (Cap: " + capacite + " | " + typeSalle + ")"; }
}
