# EvenTrack API

## Description
EvenTrack est une API REST permettant la gestion complète d'événements avec gestion des utilisateurs, des rôles, des billets et des paiements en ligne. Elle est développée en **Spring Boot** et utilise **JWT** pour l'authentification.

## Fonctionnalités principales

### 1. Gestion des utilisateurs
- ✅ Inscription et connexion avec **Spring Security + JWT**
- ✅ Rôles : **Admin, Organisateur, Participant**
- ✅ Mise à jour du profil utilisateur

### 2. Gestion des événements
- ✅ CRUD sur les événements (titre, description, date, lieu, capacité)
- ✅ Catégorisation des événements (concert, conférence, sport, etc.)
- ✅ Ajout d'une image de couverture

### 3. Réservation et gestion des billets
- ✅ Réservation d'un billet pour un événement
- ✅ Limitation du nombre de places disponibles
- ✅ Génération d'un **QR code** pour le billet
- ✅ Annulation d'une réservation

### 4. Paiement en ligne
- ✅ Intégration avec **Stripe** 
- Intégration avec **PayPal**
- Gestion des remboursements

### 5. Notifications et rappels
- Envoi d'un email de confirmation après réservation
- Rappel automatique par **email/SMS** avant l'événement

### 6. Modération et gestion des événements
- Validation ou refus d'un événement par un admin
- Suppression d'événements frauduleux

### 7. Statistiques & Dashboard (optionnel)
- Nombre de participants à un événement
- Taux de remplissage des événements
- Popularité des événements

### 8. Gestion des plannings
- Planification des événements
- Affichage d'un calendrier avec les événements

## Endpoints principaux (REST API)

| Méthode | Endpoint                  | Description                             |
|---------|---------------------------|-----------------------------------------|
| POST    | `/auth/register`          | Inscription d’un utilisateur            |
| POST    | `/auth/login`             | Connexion et récupération du token      |
| GET     | `/events`                 | Lister tous les événements              |
| GET     | `/events/{id}`            | Récupérer un événement spécifique       |
| POST    | `/events`                 | Créer un événement (organisateur)       |
| PUT     | `/events/{id}`            | Modifier un événement                   |
| DELETE  | `/events/{id}`            | Supprimer un événement                  |
| POST    | `/events/{id}/reserve`    | Réserver un billet                      |
| GET     | `/users/{id}/tickets`     | Voir les billets d’un utilisateur       |
| DELETE  | `/tickets/{id}`           | Annuler une réservation                 |
| POST    | `/events/{id}/approve`    | Approuver un événement (admin)          |
| POST    | `/events/{id}/reject`     | Refuser un événement (admin)            |


## Installation
### Prérequis
- **Java 17+**
- **Spring Boot 3+**
- **PostgreSQL/MySQL** pour la base de données
- **Maven**

### Installation du projet
```sh
git clone https://github.com/IronNetta/EventFlow.git
cd EventFlow
mvn clean install
```

### Configuration
Modifier le fichier **application.yml** avec vos propres valeurs :
```properties
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eventrack
    username: Username
    password: Password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
server:
  port: 8080
```

## Lancement
```sh
mvn spring-boot:run
```

## Exemples d'utilisation de l'API

### 1. Inscription d'un utilisateur
```http
POST /api/auth/register
Content-Type: application/json
{
  "username": "johndoe",
  "email": "johndoe@example.com",
  "password": "securepassword"
}
```

### 2. Connexion d'un utilisateur
```http
POST /api/auth/login
Content-Type: application/json
{
  "email": "johndoe@example.com",
  "password": "securepassword"
}
```
**Réponse :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5c..."
}
```

### 3. Création d'un événement (Organisateur)
```http
POST /api/events
Authorization: Bearer {token}
Content-Type: application/json
{
  "title": "Conférence Tech",
  "description": "Une conférence sur les nouvelles technologies",
  "date": "2025-06-15",
  "location": "Paris, France",
  "capacity": 200,
  "category": "Conférence"
}
```

## Documentation API
La documentation complète est disponible via **Swagger** :
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Licence
MIT License - Voir le fichier `LICENSE` pour plus de détails.

