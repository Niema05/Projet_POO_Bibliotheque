# Système de Gestion de Bibliothèque - POO Java

Un système complet de gestion de bibliothèque développé en Java avec JavaFX et MySQL.

##  Architecture

Le projet suit une **architecture en couches stricte** :

- **Couche Modèle** : Entités métier (Livre, Membre, Emprunt)
- **Couche DAO** : Accès aux données avec CRUD
- **Couche Service** : Logique métier et validations
- **Couche Contrôleur** : Interface JavaFX

##  Structure du Projet

```
src/main/java/com/bibliotheque/
├── model/
│   ├── Document.java (classe abstraite)
│   ├── Empruntable.java (interface)
│   ├── Livre.java
│   ├── Magazine.java
│   ├── Personne.java (classe abstraite)
│   ├── Membre.java
│   └── Emprunt.java
│
├── dao/
│   ├── DAO.java (interface générique)
│   ├── LivreDAO.java
│   ├── MembreDAO.java
│   ├── EmpruntDAO.java
│   └── impl/
│       ├── LivreDAOImpl.java
│       ├── MembreDAOImpl.java
│       └── EmpruntDAOImpl.java
│
├── service/
│   ├── BibliothequeService.java
│   └── EmpruntService.java
│
├── controller/
│   ├── MainController.java
│   ├── LivreController.java
│   ├── MembreController.java
│   └── EmpruntController.java
│
├── util/
│   ├── DatabaseConnection.java (Singleton)
│   ├── StringValidator.java
│   └── DateUtils.java
│
├── exception/
│   ├── ValidationException.java
│   ├── LivreIndisponibleException.java
│   ├── MembreInactifException.java
│   └── LimiteEmpruntDepasseeException.java
│
└── Main.java
```

##  Installation et Configuration

### Prérequis
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Étapes d'installation

1. **Créer la base de données MySQL**
   ```bash
   mysql -u root -p < database.sql
   ```

2. **Configurer la connexion à la base de données**
   - Éditer `src/main/java/com/bibliotheque/util/DatabaseConnection.java`
   - Modifier les paramètres de connexion si nécessaire

3. **Compiler le projet**
   ```bash
   mvn clean compile
   ```

4. **Construire le JAR**
   ```bash
   mvn package
   ```

5. **Lancer l'application**
   ```bash
   mvn javafx:run
   ```

## Fonctionnalités

### Gestion des Livres
-  Ajouter/Modifier/Supprimer des livres
-  Rechercher des livres par titre ou auteur
-  Afficher la disponibilité des livres
-  Validation des données (ISBN, titre, année)

### Gestion des Membres
-  Ajouter/Modifier/Désactiver des membres
-  Rechercher des membres
-  Gestion du statut actif/inactif
-  Validation des emails

### Gestion des Emprunts
-  Emprunter un livre (max 3 emprunts par membre)
-  Retourner un livre
-  Calculer les pénalités de retard
-  Afficher les emprunts en cours ou en retard
-  Historique des emprunts par membre

##  Règles Métier

### Emprunt d'un livre
- Vérifier que le membre est **actif**
- Vérifier que le livre est **disponible**
- Vérifier que le membre n'a pas déjà **3 emprunts en cours**
- Durée d'emprunt : **14 jours**

### Pénalités de retard
- Livre : **2 DH par jour** de retard
- Magazine : **1 DH par jour** de retard

##  Schéma de Base de Données

### Table `livres`
```sql
isbn VARCHAR(20) PRIMARY KEY
titre VARCHAR(200) NOT NULL
auteur VARCHAR(100) NOT NULL
annee_publication INT
disponible BOOLEAN DEFAULT TRUE
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
```

### Table `membres`
```sql
id INT PRIMARY KEY AUTO_INCREMENT
nom VARCHAR(50) NOT NULL
prenom VARCHAR(50) NOT NULL
email VARCHAR(100) UNIQUE NOT NULL
actif BOOLEAN DEFAULT TRUE
date_inscription DATE DEFAULT CURRENT_DATE
```

### Table `emprunts`
```sql
id INT PRIMARY KEY AUTO_INCREMENT
isbn VARCHAR(20) NOT NULL
membre_id INT NOT NULL
date_emprunt DATE NOT NULL
date_retour_prevue DATE NOT NULL
date_retour_effective DATE NULL
penalite DOUBLE DEFAULT 0
FOREIGN KEY (isbn) REFERENCES livres(isbn)
FOREIGN KEY (membre_id) REFERENCES membres(id)
```

##  Dépendances

- **JavaFX** 21 : Interface graphique
- **MySQL Connector** 8.0.33 : Connecteur MySQL
- **JUnit 5** : Tests unitaires
- **Maven** : Gestion des dépendances

## 🎓 Concepts POO Utilisés

-  **Héritage** : Document, Personne
-  **Polymorphisme** : Empruntable interface
-  **Encapsulation** : Attributs private avec getters/setters
-  **Abstraction** : Classes abstraites et interfaces
-  **Design Patterns** : Singleton (DatabaseConnection), DAO, MVC
-  **Gestion d'exceptions** : Exceptions personnalisées
-  **Thread-safety** : Double-Checked Locking pour Singleton

##  Sécurité

- Utilisation de **PreparedStatement** pour éviter les injections SQL
- Validation des données avant insertion en base
- Gestion sécurisée de la connexion à la base de données

##  Exemples d'Utilisation

### Ajouter un livre
```java
Livre livre = new Livre("978-2070361563", "Le Seigneur des Anneaux", 
                        "J.R.R. Tolkien", 1954, true);
bibliothequeService.ajouterLivre(livre);
```

### Emprunter un livre
```java
try {
    Emprunt emprunt = empruntService.emprunterLivre("978-2070361563", 1);
    System.out.println("Emprunt créé : " + emprunt);
} catch (MembreInactifException | LivreIndisponibleException e) {
    System.err.println("Erreur : " + e.getMessage());
}
```

##  Licence

Ce projet est un travail académique pour la formation en POO Java.

##  Auteur

Créé à titre éducatif pour démontrer les principes de la programmation orientée objet en Java.
