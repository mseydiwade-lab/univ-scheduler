-- Script SQLite pour UNIV-SCHEDULER

DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS emploi_equipement;
DROP TABLE IF EXISTS emplois_du_temps;
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS salle_equipement;
DROP TABLE IF EXISTS equipements;
DROP TABLE IF EXISTS salles;
DROP TABLE IF EXISTS batiments;
DROP TABLE IF EXISTS utilisateurs;

CREATE TABLE utilisateurs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom_utilisateur TEXT UNIQUE NOT NULL,
    mot_de_passe TEXT NOT NULL,
    email TEXT NOT NULL,
    prenom TEXT NOT NULL,
    nom TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('ADMIN', 'GESTIONNAIRE', 'ENSEIGNANT', 'ETUDIANT')),
    departement TEXT,
    groupe_classe TEXT,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    est_actif INTEGER DEFAULT 1
);

CREATE TABLE batiments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    code TEXT UNIQUE NOT NULL,
    localisation TEXT,
    nombre_etages INTEGER DEFAULT 1,
    description TEXT,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE salles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    batiment_id INTEGER NOT NULL,
    numero_salle TEXT NOT NULL,
    nom TEXT,
    etage INTEGER DEFAULT 0,
    capacite INTEGER NOT NULL,
    type_salle TEXT NOT NULL CHECK (type_salle IN ('TD', 'TP', 'AMPHI', 'REUNION')),
    superficie REAL,
    est_active INTEGER DEFAULT 1,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batiment_id) REFERENCES batiments(id) ON DELETE CASCADE,
    UNIQUE (batiment_id, numero_salle)
);

CREATE TABLE equipements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    type TEXT NOT NULL,
    description TEXT,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE salle_equipement (
    salle_id INTEGER NOT NULL,
    equipement_id INTEGER NOT NULL,
    quantite INTEGER DEFAULT 1,
    PRIMARY KEY (salle_id, equipement_id),
    FOREIGN KEY (salle_id) REFERENCES salles(id) ON DELETE CASCADE,
    FOREIGN KEY (equipement_id) REFERENCES equipements(id) ON DELETE CASCADE
);

CREATE TABLE cours (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT UNIQUE NOT NULL,
    intitule TEXT NOT NULL,
    description TEXT,
    enseignant_id INTEGER,
    departement TEXT,
    credits INTEGER DEFAULT 3,
    heures_par_semaine INTEGER DEFAULT 2,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enseignant_id) REFERENCES utilisateurs(id) ON DELETE SET NULL
);

CREATE TABLE emplois_du_temps (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cours_id INTEGER NOT NULL,
    salle_id INTEGER NOT NULL,
    jour_semaine INTEGER NOT NULL CHECK (jour_semaine BETWEEN 1 AND 7),
    heure_debut TEXT NOT NULL,
    duree_minutes INTEGER NOT NULL DEFAULT 120,
    groupe_classe TEXT,
    annee_academique TEXT,
    semestre INTEGER CHECK (semestre IN (1, 2)),
    est_recurrent INTEGER DEFAULT 1,
    date_debut TEXT,
    date_fin TEXT,
    cree_par INTEGER,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    FOREIGN KEY (salle_id) REFERENCES salles(id) ON DELETE CASCADE,
    FOREIGN KEY (cree_par) REFERENCES utilisateurs(id) ON DELETE SET NULL
);

CREATE TABLE reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    salle_id INTEGER NOT NULL,
    utilisateur_id INTEGER NOT NULL,
    titre TEXT NOT NULL,
    description TEXT,
    date_reservation TEXT NOT NULL,
    heure_debut TEXT NOT NULL,
    heure_fin TEXT NOT NULL,
    statut TEXT DEFAULT 'EN_ATTENTE' CHECK (statut IN ('EN_ATTENTE', 'APPROUVE', 'REJETE', 'ANNULE')),
    motif TEXT,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (salle_id) REFERENCES salles(id) ON DELETE CASCADE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    titre TEXT NOT NULL,
    message TEXT NOT NULL,
    type TEXT CHECK (type IN ('INFO', 'AVERTISSEMENT', 'ERREUR', 'SUCCES')),
    est_lu INTEGER DEFAULT 0,
    cree_le DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE INDEX idx_emplois_salle_jour ON emplois_du_temps(salle_id, jour_semaine);
CREATE INDEX idx_reservations_salle ON reservations(salle_id, date_reservation);
CREATE INDEX idx_utilisateurs_role ON utilisateurs(role);
CREATE INDEX idx_salles_batiment ON salles(batiment_id);

INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, email, prenom, nom, role, departement, groupe_classe) VALUES
('admin', 'password123', 'admin@univ.edu', 'Admin', 'Systeme', 'ADMIN', null, null),
('gestionnaire', 'password123', 'gestionnaire@univ.edu', 'Fatou', 'WADE', 'GESTIONNAIRE', null, null),
('prof1', 'password123', 'prof1@univ.edu', 'Abdoulaye', 'Diallo', 'ENSEIGNANT', 'Informatique', null),
('prof2', 'password123', 'prof2@univ.edu', 'Mohamed', 'Diagne', 'ENSEIGNANT', 'Informatique', null),
('etudiant1', 'password123', 'etudiant1@univ.edu', 'Bakary', 'Sagna', 'ETUDIANT', 'Informatique', 'L2-INFO-A');

INSERT INTO batiments (nom, code, localisation, nombre_etages, description) VALUES
('Batiment Principal', 'BAT-A', 'Centre du campus', 4, 'Batiment historique avec amphitheatres'),
('Batiment SET', 'BAT-SET', 'Nord du campus', 3, 'Salles de TP et laboratoires'),
('Batiment SES', 'BAT-SES', 'Sud du campus', 2, 'Salles de TD et bibliotheque');

INSERT INTO salles (batiment_id, numero_salle, nom, etage, capacite, type_salle) VALUES
(1, '101', 'Amphitheatre A', 1, 200, 'AMPHI'),
(1, '102', 'Amphitheatre B', 1, 150, 'AMPHI'),
(1, '201', 'Salle TD 201', 2, 40, 'TD'),
(1, '202', 'Salle TD 202', 2, 35, 'TD'),
(2, 'LAB1', 'Laboratoire Informatique 1', 1, 25, 'TP'),
(2, 'LAB2', 'Laboratoire Informatique 2', 1, 25, 'TP'),
(2, '301', 'Salle Chimie', 3, 30, 'TP'),
(3, 'L101', 'Salle Conference', 1, 50, 'TD'),
(3, 'L201', 'Salle de Reunion', 2, 20, 'REUNION');

INSERT INTO equipements (nom, type, description) VALUES
('Videoprojecteur', 'VISUEL', 'Projecteur HD standard'),
('Tableau interactif', 'INTERACTIF', 'Tableau tactile Smart Board'),
('Ordinateurs', 'INFORMATIQUE', 'Postes informatiques fixes'),
('Microscope', 'SCIENCE', 'Microscope optique professionnel'),
('Climatisation', 'CONFORT', 'Systeme de climatisation'),
('Systeme audio', 'AUDIO', 'Enceintes et micro sans fil');

INSERT INTO salle_equipement (salle_id, equipement_id, quantite) VALUES
(1, 1, 2), (1, 5, 1), (1, 6, 1),
(2, 1, 1), (2, 5, 1),
(3, 1, 1), (3, 2, 1),
(4, 1, 1),
(5, 1, 1), (5, 3, 20), (5, 5, 1),
(6, 1, 1), (6, 3, 20), (6, 5, 1),
(7, 4, 10), (7, 5, 1),
(8, 1, 1), (8, 2, 1),
(9, 1, 1);

INSERT INTO cours (code, intitule, description, enseignant_id, departement, credits, heures_par_semaine) VALUES
('INFO101', 'Introduction a la Programmation', 'Bases de la programmation Java', 3, 'Informatique', 6, 4),
('INFO201', 'Bases de Donnees', 'Conception et SQL', 3, 'Informatique', 6, 4),
('PHY101', 'Physique Quantique', 'Introduction a la mecanique quantique', 4, 'Physique', 6, 4),
('CHM101', 'Chimie Organique', 'Reactions organiques de base', 4, 'Chimie', 6, 3);

INSERT INTO emplois_du_temps (cours_id, salle_id, jour_semaine, heure_debut, duree_minutes, groupe_classe, annee_academique, semestre) VALUES
(1, 5, 1, '08:00', 120, 'L2-INFO-A', '2025-2026', 2),
(1, 5, 3, '10:00', 120, 'L2-INFO-A', '2025-2026', 2),
(2, 6, 2, '14:00', 120, 'L2-INFO-A', '2025-2026', 2),
(3, 1, 1, '10:00', 120, 'L2-PHYS', '2025-2026', 2),
(4, 7, 4, '08:00', 180, 'L2-CHIM', '2025-2026', 2);

INSERT INTO notifications (utilisateur_id, titre, message, type) VALUES
(1, 'Bienvenue !', 'Bienvenue sur UNIV-SCHEDULER', 'INFO'),
(2, 'Bienvenue !', 'Bienvenue sur UNIV-SCHEDULER', 'INFO'),
(3, 'Bienvenue !', 'Bienvenue sur UNIV-SCHEDULER', 'INFO'),
(4, 'Bienvenue !', 'Bienvenue sur UNIV-SCHEDULER', 'INFO'),
(5, 'Bienvenue !', 'Bienvenue sur UNIV-SCHEDULER', 'INFO');