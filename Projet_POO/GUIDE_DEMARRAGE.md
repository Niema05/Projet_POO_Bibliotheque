# Guide de Démarrage - Système de Gestion de Bibliothèque

## ✅ Checklist de Configuration Initiale

### 1. Préparer MySQL
- [ ] MySQL est installé et en cours d'exécution
- [ ] Créer la base de données en exécutant le script SQL :
  ```bash
  mysql -u root -p < database.sql
  ```
- [ ] Vérifier la connexion :
  ```bash
  mysql -u root -p -e "USE bibliotheque; SELECT COUNT(*) FROM livres;"
  ```

### 2. Configurer le Projet
- [ ] Java 17+ est installé
- [ ] Maven est installé
- [ ] Le fichier `DatabaseConnection.java` est configuré avec les bons paramètres

### 3. Construire le Projet
- [ ] Compiler avec Maven :
  ```bash
  mvn clean install
  ```
- [ ] Vérifier qu'aucune erreur ne s'affiche

### 4. Lancer l'Application
- [ ] Exécuter la commande :
  ```bash
  mvn javafx:run
  ```
- [ ] L'interface graphique doit s'afficher

## 🎯 Guide d'Utilisation

### Onglet "Livres"
1. **Ajouter un livre** :
   - Remplir le formulaire avec un ISBN au format 978-XXXXXXXXXX
   - Cliquer sur "Ajouter"

2. **Rechercher des livres** :
   - Entrer un titre ou auteur
   - Cliquer sur "Rechercher"

3. **Modifier un livre** :
   - Sélectionner un livre dans le tableau
   - Modifier les informations
   - Cliquer sur "Modifier"

4. **Supprimer un livre** :
   - Sélectionner un livre
   - Cliquer sur "Supprimer"

### Onglet "Membres"
1. **Ajouter un membre** :
   - Remplir nom, prénom et email valide
   - Cliquer sur "Ajouter"

2. **Rechercher des membres** :
   - Entrer un nom, prénom ou email
   - Cliquer sur "Rechercher"

3. **Modifier un membre** :
   - Sélectionner un membre
   - Modifier les informations
   - Cliquer sur "Modifier"

4. **Désactiver un membre** :
   - Sélectionner un membre
   - Cliquer sur "Désactiver"

### Onglet "Emprunts"
1. **Créer un emprunt** :
   - Sélectionner un livre dans le combo "Livre"
   - Sélectionner un membre dans le combo "Membre"
   - Cliquer sur "Emprunter"
   - Une alerte confirmera le succès

2. **Retourner un livre** :
   - Sélectionner un emprunt dans le tableau
   - Cliquer sur "Retourner"
   - La pénalité sera calculée automatiquement

3. **Filtrer les emprunts** :
   - **Tous les emprunts** : Affiche tous les emprunts
   - **En cours** : Affiche les emprunts non retournés
   - **En retard** : Affiche les emprunts retournés en retard

## ⚠️ Règles et Validations

### Validation des Données
- **ISBN** : Format strict 978-XXXXXXXXXX
- **Email** : Format valide (exemple@mail.com)
- **Année** : Entre 1000 et l'année actuelle
- **Champs obligatoires** : Tous les champs doivent être remplis

### Règles Métier
- Un membre peut emprunter **maximum 3 livres** à la fois
- La durée d'emprunt est de **14 jours**
- Un **membre inactif** ne peut pas emprunter
- Un **livre indisponible** ne peut pas être emprunté
- Les pénalités de retard sont calculées automatiquement

## 🛠️ Dépannage

### Erreur : "Connection refused"
**Solution** : Vérifier que MySQL est en cours d'exécution
```bash
# Windows
net start MySQL80

# Linux/Mac
sudo service mysql start
```

### Erreur : "Access denied for user 'root'@'localhost'"
**Solution** : Vérifier les paramètres de connexion dans `DatabaseConnection.java`
- URL : `jdbc:mysql://localhost:3306/bibliotheque`
- USER : `root`
- PASSWORD : (mot de passe correct)

### Erreur : "Unknown database 'bibliotheque'"
**Solution** : Exécuter le script SQL :
```bash
mysql -u root -p < database.sql
```

### L'interface n'affiche pas les données
**Solution** :
1. Vérifier la connexion à la base de données
2. Vérifier que les données existent dans les tables
3. Redémarrer l'application

## 📊 Inspection de la Base de Données

Pour vérifier l'état de la base de données :
```sql
-- Voir tous les livres
SELECT * FROM livres;

-- Voir tous les membres
SELECT * FROM membres;

-- Voir tous les emprunts
SELECT * FROM emprunts;

-- Voir les emprunts en retard
SELECT e.*, l.titre, m.nom, m.prenom
FROM emprunts e
JOIN livres l ON e.isbn = l.isbn
JOIN membres m ON e.membre_id = m.id
WHERE e.date_retour_effective > e.date_retour_prevue
OR (e.date_retour_effective IS NULL AND e.date_retour_prevue < CURDATE());
```

## 📚 Structure des Fichiers Importants

```
src/main/java/com/bibliotheque/
├── Main.java              # Point d'entrée de l'application
├── model/                 # Classes métier
├── dao/                   # Accès aux données
├── service/               # Logique métier
├── controller/            # Contrôleurs JavaFX
├── util/                  # Utilitaires
└── exception/             # Exceptions personnalisées

src/main/resources/fxml/
├── main.fxml              # Interface principale
├── livres.fxml            # Tab des livres
├── membres.fxml           # Tab des membres
└── emprunts.fxml          # Tab des emprunts

database.sql               # Script de création BD
```

## 🎓 Points de Personnalisation

### Modifier les limites d'emprunt
Éditer `EmpruntService.java` :
```java
private static final int LIMITE_EMPRUNTS = 3;      // Changer 3
private static final int JOURS_EMPRUNT = 14;        // Changer 14
```

### Modifier les pénalités
Éditer les classes de modèle :
```java
// Livre.java
@Override
public double calculerPenaliteRetard() {
    return 2.0;  // 2 DH par jour
}

// Magazine.java
@Override
public double calculerPenaliteRetard() {
    return 1.0;  // 1 DH par jour
}
```

### Personnaliser l'interface
Les fichiers FXML se trouvent dans `src/main/resources/fxml/`

## 📞 Support

Pour toute question :
1. Vérifier les logs dans la console
2. Consulter le README.md pour plus de détails
3. Examiner les messages d'erreur affichés

Bonne utilisation du système ! 🎉
