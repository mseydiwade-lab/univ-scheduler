# UNIV-SCHEDULER
## Application de Gestion Intelligente des Salles et des Emplois du Temps

---

## 📋 Description
UNIV-SCHEDULER est une application Java permettant de gérer les salles et emplois du temps d'une université.

## 👥 Comptes de test
| Utilisateur | Mot de passe | Rôle |
|-------------|-------------|------|
| admin | password123 | Administrateur |
| gestionnaire1 | password123 | Gestionnaire |
| prof1 | password123 | Enseignant |
| etudiant1 | password123 | Étudiant |

## 🚀 Installation

### Prérequis
- Java JDK 21+
- IntelliJ IDEA

### Étapes
1. Ouvrir le projet dans IntelliJ IDEA
2. Ajouter le driver SQLite JDBC (`sqlite-jdbc-3.43.0.0.jar`) via File > Project Structure > Libraries
3. Lancer `Main.java`

## 🏗️ Architecture
```
src/
├── Main.java
├── database/
│   └── DatabaseConnection.java
├── models/
│   ├── Utilisateur.java
│   ├── Batiment.java
│   ├── Salle.java
│   ├── Cours.java
│   ├── EmploiDuTemps.java
│   ├── Reservation.java
│   └── Notification.java
├── dao/
│   ├── UtilisateurDAO.java
│   ├── BatimentDAO.java
│   ├── SalleDAO.java
│   ├── CoursDAO.java
│   ├── EmploiDuTempsDAO.java
│   ├── ReservationDAO.java
│   └── NotificationDAO.java
└── views/
    ├── Theme.java
    ├── VueBase.java
    ├── LoginView.java
    ├── MainView.java
    ├── DashboardView.java
    ├── BatimentView.java
    ├── SalleView.java
    ├── CoursView.java
    ├── EmploiDuTempsView.java
    ├── ReservationView.java
    ├── NotificationView.java
    └── UtilisateurView.java
```

## ✅ Fonctionnalités
- Gestion des bâtiments et salles
- Gestion des cours et emplois du temps
- Détection automatique des conflits
- Réservations ponctuelles de salles
- Système de notifications
- Dashboard avec statistiques en temps réel
- Gestion des utilisateurs (Admin)
- Recherche de salles disponibles

## 🛠️ Technologies
- Java 21
- Swing (interface graphique)
- SQLite (base de données)
- JDBC (connexion base de données)
