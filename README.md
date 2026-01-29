# 🍽️ Mini Dish - Système de Gestion de Restaurant

[![Database](https://img.shields.io/badge/Database-PostgreSQL-336791?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Status](https://img.shields.io/badge/Status-Database_Ready-green?style=for-the-badge)](https://github.com/)

## 📝 Présentation du Projet
Mini Dish est une application conçue pour optimiser la gestion d'un restaurant. Ce projet couvre l'intégralité de la chaîne de valeur : de la gestion du catalogue des plats à la traçabilité précise des stocks d'ingrédients.

---

## 🏗️ Architecture de la Base de Données

L'étape actuelle se concentre sur la fondation du projet : une structure SQL robuste et normalisée.

### Points Forts de la Modélisation :
- **Types Énumérés (ENUMs) :** Sécurisation des données via des listes de choix strictes (`starter`, `main`, `dessert`, etc.).
- **Gestion de Stock Avancée :** Utilisation d'une table de mouvements (`stock_movement`) permettant une traçabilité `IN/OUT` en temps réel.
- **Normalisation Many-to-Many :** La table `dish_ingredient` permet de dissocier les recettes des ingrédients, offrant une flexibilité maximale.

### Schéma de l'entité-relation (ERD) :
> *Note : Imaginez ici une relation fluide entre Plats, Ingrédients et Commandes.*

---

## 🚀 Installation & Configuration

### 1. Prérequis
- PostgreSQL (v15+)
- Terminal Ubuntu / Linux

### 2. Initialisation de la Base de Données
Exécutez les scripts dans l'ordre suivant :

```bash
# Création de l'utilisateur et de la base (en tant que super-admin)
sudo -u postgres psql -f src/main/resources/sql/db.sql

# Déploiement du schéma (en tant que manager)
psql -h localhost -U mini_dish_db_manager -d mini_dish_db -f src/main/resources/sql/schema.sql

# Insertion des données de test
psql -h localhost -U mini_dish_db_manager -d mini_dish_db -f src/main/resources/sql/data.sql