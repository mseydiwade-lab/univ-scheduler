package models;

public class EmploiDuTemps {
    private int id;
    private int coursId;
    private int salleId;
    private int jourSemaine;
    private String heureDebut;
    private int dureeMinutes;
    private String groupeClasse;
    private String anneeAcademique;
    private int semestre;
    private boolean estRecurrent;
    private String dateDebut;
    private String dateFin;
    private Cours cours;
    private Salle salle;

    public EmploiDuTemps() {}

    public EmploiDuTemps(int coursId, int salleId, int jourSemaine,
                         String heureDebut, int dureeMinutes, String groupeClasse,
                         String anneeAcademique, int semestre) {
        this.coursId = coursId;
        this.salleId = salleId;
        this.jourSemaine = jourSemaine;
        this.heureDebut = heureDebut;
        this.dureeMinutes = dureeMinutes;
        this.groupeClasse = groupeClasse;
        this.anneeAcademique = anneeAcademique;
        this.semestre = semestre;
        this.estRecurrent = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCoursId() { return coursId; }
    public void setCoursId(int coursId) { this.coursId = coursId; }
    public int getSalleId() { return salleId; }
    public void setSalleId(int salleId) { this.salleId = salleId; }
    public int getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(int jourSemaine) { this.jourSemaine = jourSemaine; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public int getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(int dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public String getGroupeClasse() { return groupeClasse; }
    public void setGroupeClasse(String groupeClasse) { this.groupeClasse = groupeClasse; }
    public String getAnneeAcademique() { return anneeAcademique; }
    public void setAnneeAcademique(String anneeAcademique) { this.anneeAcademique = anneeAcademique; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public boolean isEstRecurrent() { return estRecurrent; }
    public void setEstRecurrent(boolean estRecurrent) { this.estRecurrent = estRecurrent; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }
    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }

    public String getJourNom() {
        String[] jours = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        return (jourSemaine >= 1 && jourSemaine <= 7) ? jours[jourSemaine] : "?";
    }

    @Override
    public String toString() { return getJourNom() + " " + heureDebut + " - " + groupeClasse; }
}
