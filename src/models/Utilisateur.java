package models;

public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private String email;
    private String prenom;
    private String nom;
    private String role;
    private String departement;
    private String groupeClasse;
    private boolean estActif;

    public Utilisateur() {}

    public Utilisateur(String nomUtilisateur, String motDePasse, String email,
                       String prenom, String nom, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.estActif = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
    public String getGroupeClasse() { return groupeClasse; }
    public void setGroupeClasse(String groupeClasse) { this.groupeClasse = groupeClasse; }
    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }

    @Override
    public String toString() { return prenom + " " + nom + " (" + role + ")"; }
}
