package models;

public class Batiment {
    private int id;
    private String nom;
    private String code;
    private String localisation;
    private int nombreEtages;
    private String description;

    public Batiment() {}

    public Batiment(String nom, String code, String localisation, int nombreEtages) {
        this.nom = nom;
        this.code = code;
        this.localisation = localisation;
        this.nombreEtages = nombreEtages;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public int getNombreEtages() { return nombreEtages; }
    public void setNombreEtages(int nombreEtages) { this.nombreEtages = nombreEtages; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() { return nom + " (" + code + ")"; }
}
