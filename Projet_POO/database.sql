-- Script de création de la base de données Bibliothèque

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;

-- Créer la table des livres
CREATE TABLE IF NOT EXISTS livres (
    isbn VARCHAR(20) PRIMARY KEY,
    titre VARCHAR(200) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    annee_publication INT NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Créer la table des membres
CREATE TABLE IF NOT EXISTS membres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    date_inscription DATE DEFAULT (CURDATE())
);

-- Créer la table des emprunts
CREATE TABLE IF NOT EXISTS emprunts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20) NOT NULL,
    membre_id INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE NULL,
    penalite DOUBLE DEFAULT 0,
    FOREIGN KEY (isbn) REFERENCES livres(isbn) ON DELETE CASCADE,
    FOREIGN KEY (membre_id) REFERENCES membres(id) ON DELETE CASCADE,
    INDEX idx_emprunt_en_cours (date_retour_effective)
);

-- Insérer quelques données de test
INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES
('978-2070361563', 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 1954, TRUE),
('978-2070368945', 'Harry Potter à l\'école des sorciers', 'J.K. Rowling', 1998, TRUE),
('978-2253121138', 'Les Misérables', 'Victor Hugo', 1862, FALSE);

INSERT INTO membres (nom, prenom, email, actif, date_inscription) VALUES
('Dupont', 'Jean', 'jean.dupont@example.com', TRUE, '2024-01-15'),
('Martin', 'Marie', 'marie.martin@example.com', TRUE, '2024-02-20'),
('Bernard', 'Pierre', 'pierre.bernard@example.com', FALSE, '2024-03-10');

-- Afficher les statistiques
SELECT 'Base de données créée avec succès!' AS message;
SELECT COUNT(*) AS nombre_livres FROM livres;
SELECT COUNT(*) AS nombre_membres FROM membres;
