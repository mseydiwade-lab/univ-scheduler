package models;

public class Cours {
    private int id;
    private String code;
    private String intitule;
    private String description;
    private int enseignantId;
    private String departement;
    private int credits;
    private int heuresParSemaine;
    private Utilisateur enseignant;

    public Cours() {}

    public Cours(String code, String intitule, int enseignantId,
                 String departement, int credits, int heuresParSemaine) {
        this.code = code;
        this.intitule = intitule;
        this.enseignantId = enseignantId;
        this.departement = departement;
        this.credits = credits;
        this.heuresParSemaine = heuresParSemaine;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getEnseignantId() { return enseignantId; }
    public void setEnseignantId(int enseignantId) { this.enseignantId = enseignantId; }
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public int getHeuresParSemaine() { return heuresParSemaine; }
    public void setHeuresParSemaine(int h) { this.heuresParSemaine = h; }
    public Utilisateur getEnseignant() { return enseignant; }
    public void setEnseignant(Utilisateur enseignant) { this.enseignant = enseignant; }

    @Override
    public String toString() { return "[" + code + "] " + intitule; }
}
