# 🍽️ Mini Dish - Système de Gestion de Restaurant

[![Database](https://img.shields.io/badge/Database-PostgreSQL-336791?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)

## 📝 Présentation du Projet
Mini Dish est une application de gestion de restaurant couvrant le catalogue des plats, la traçabilité des stocks et le cycle de vie des commandes.

---

## 🛠️ Implémentation JDBC (Java Database Connectivity)

La couche d'accès aux données (DAO) repose sur une architecture JDBC pure pour garantir performance et contrôle.

### 🔑 Points Clés du `DataRetriever` :
- **Gestion des Connexions :** Utilisation du **Try-with-resources** pour garantir la fermeture automatique des `Connection`, `Statement` et `ResultSet`, évitant ainsi les fuites de ressources.
- **Sécurité :** Utilisation systématique de **PreparedStatement** pour prévenir les injections SQL et optimiser les performances via le pré-parsing des requêtes.
- **Mise à jour Atomique (UPSERT) :** Implémentation de la clause `ON CONFLICT` de PostgreSQL dans les méthodes `save` pour gérer l'insertion et la mise à jour en une seule transaction.
- **Mapping Objet-Relationnel Manuel :** - Conversion des types `Timestamp` SQL vers `Instant` Java.
    - Mapping des chaînes de caractères vers les types `Enum` Java (`valueOf()`).
    - Cast explicite des types énumérés SQL (ex: `?::order_status`).



---

## 🏗️ Architecture & Modélisation (K3)

### 🛡️ Gestion des Commandes
L'application gère désormais le cycle de vie complet d'une commande :
- **Types :** `EAT_IN` (Sur place) ou `TAKE_AWAY` (À emporter).
- **Statuts :** `CREATED` ➔ `READY` ➔ `DELIVERED`.
- **Règle d'Immuabilité :** Une exception `RuntimeException` est levée dans `saveOrder` si une modification est tentée sur une commande déjà livrée.

---

## 🚀 Installation & Configuration

### 1. Configuration de l'environnement
L'application utilise un fichier `.env` (ou les variables d'environnement système) pour les identifiants de connexion :
- `DB_URL`: `jdbc:postgresql://localhost:5432/mini_dish_db`
- `DB_USER`: `mini_dish_db_manager`
- `DB_PASSWORD`: `votre_mot_de_passe`

### 2. Initialisation SQL
```bash
# 1. Création de la DB
sudo -u postgres psql -f src/main/resources/sql/db.sql
# 2. Schéma et Types ENUM
psql -h localhost -U mini_dish_db_manager -d mini_dish_db -f src/main/resources/sql/schema.sql