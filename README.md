# EvenTrack API

## Description
EvenTrack est une API REST permettant la gestion complète d'événements avec gestion des utilisateurs, des rôles, des billets et des paiements en ligne. Elle est développée en **Spring Boot** et utilise **JWT** pour l'authentification.

## Fonctionnalités principales

### 1. Gestion des utilisateurs
- ✅ Inscription et connexion avec **Spring Security + JWT**
- ✅ Rôles : **Admin, Organisateur, Participant**
- ✅ Mise à jour du profil utilisateur
- ✅ Authentification à 2 facteurs

### 2. Gestion des événements
- ✅ CRUD sur les événements (titre, description, date, lieu, capacité)
- ✅ Catégorisation des événements (concert, conférence, sport, etc.)
- ✅ Ajout d'une image de couverture
- ✅ Tarification multiple des événements

### 3. Réservation et gestion des billets
- ✅ Réservation d'un billet pour un événement
- ✅ Limitation du nombre de places disponibles
- ✅ Génération d'un **QR code** pour le billet
- ✅ Annulation d'une réservation

### 4. Paiement en ligne
- ✅ Intégration avec **Stripe** 
- ✅ Intégration avec **PayPal**
- ✅ Gestion des remboursements

### 5. Notifications et rappels
- ✅ Envoi d'un email de confirmation après réservation
- ✅ Rappel automatique par **email/SMS** avant l'événement

### 6. Modération et gestion des événements
- ✅ Validation ou refus d'un événement par un admin
- ✅ Suppression d'événements frauduleux

### 7. Statistiques & Dashboard (optionnel)
- ✅ Nombre de participants à un événement
- ✅ Taux de remplissage des événements
- ✅ Popularité des événements
- (Méhtodes existes mais doivent être utilisées)

### 8. Gestion des plannings
- ✅ Planification des événements
- ✅ Affichage d'un calendrier avec les événements

### 9. Gestion des messages
- ✅ Envoi de messages entre utilisateurs
- ✅ Marquage des messages comme lus
- ✅ Suppression de messages
- ✅ Affichage des messages par utilisateur

# API Endpoints

## AuthController

| Méthode | Endpoint       | Description                     | Rôles/Requis |
|---------|--------------|---------------------------------|--------------|
| POST    | `/auth/register` | Inscription d'un utilisateur  | Anonyme      |
| POST    | `/auth/login`    | Connexion d'un utilisateur    | Anonyme      |

## PaymentController

| Méthode | Endpoint                             | Description                           | Rôles/Requis |
|---------|-------------------------------------|-------------------------------------|--------------|
| POST    | `/api/payments/create-checkout-session` | Création d'une session de paiement Stripe | - |
| POST    | `/api/payments/webhook`            | Gestion du webhook Stripe           | - |

## EventController

| Méthode | Endpoint                  | Description                         | Rôles/Requis        |
|---------|--------------------------|-----------------------------------|--------------------|
| GET     | `/events`                | Récupère tous les événements      | Public             |
| GET     | `/events/{id}`           | Récupère un événement par ID      | Public             |
| POST    | `/events`                | Créer un événement               | ADMIN, ORGANIZER   |
| PUT     | `/events/{id}`           | Met à jour un événement          | ADMIN, ORGANIZER   |
| DELETE  | `/events/{id}`           | Supprime un événement            | ADMIN              |
| PUT     | `/events/{id}/accept`    | Accepte un événement             | ADMIN              |
| PUT     | `/events/{id}/reject`    | Rejette un événement             | ADMIN              |
| GET     | `/events/planning`       | Récupère le planning des événements | Authentifié   |
| PUT     | `/events/{id}/planify`   | Planifie un événement            | ADMIN              |
| GET     | `/events/popularity/{id}` | Obtient la popularité d'un événement | ORGANIZER     |

## MessageController

| Méthode | Endpoint            | Description                  | Rôles/Requis |
|---------|--------------------|------------------------------|--------------|
| POST    | `/messages/send`   | Envoie un message            | Authentifié  |
| GET     | `/messages/received` | Récupère les messages reçus  | Authentifié  |
| GET     | `/messages/sent`   | Récupère les messages envoyés | Authentifié  |
| PUT     | `/messages/{id}/read` | Marque un message comme lu  | Authentifié  |
| DELETE  | `/messages/{id}`   | Supprime un message         | Authentifié  |

## TicketController

| Méthode | Endpoint          | Description                | Rôles/Requis |
|---------|------------------|----------------------------|--------------|
| GET     | `/tickets`       | Récupère tous les tickets  | Authentifié  |
| POST    | `/tickets/book`  | Réserve un ticket          | Authentifié  |
| GET     | `/tickets/{id}`  | Récupère un ticket par ID  | Authentifié  |
| DELETE  | `/tickets/cancel/{id}` | Annule un ticket | Authentifié |

## UserController

| Méthode | Endpoint         | Description                  | Rôles/Requis |
|---------|-----------------|------------------------------|--------------|
| GET     | `/user`         | Récupère tous les utilisateurs | ADMIN        |
| GET     | `/user/{email}` | Récupère un utilisateur par email | ADMIN        |
| POST    | `/user`         | Crée un utilisateur          | ADMIN        |
| PUT     | `/user/{email}` | Met à jour un utilisateur    | ADMIN        |
| DELETE  | `/user/{email}` | Supprime un utilisateur      | ADMIN        |

## Installation
### Prérequis
- **Java 17+**
- **Spring Boot 3+**
- **PostgreSQL/MySQL** pour la base de données
- **Maven**

### Installation du projet
```sh
git clone https://github.com/IronNetta/EvenTrack.git
cd EvenTrack
mvn clean install
```

### Configuration
Modifier le fichier **application.properties** avec vos propres valeurs :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eventrack
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

stripe.secretKey=your_stripe_secret_key

spring.mail.host=smtp_host
spring.mail.port=your_smtp_port
spring.mail.username=your_email
spring.mail.password=your_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

server.port=your_port
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
  "location": "Paris, France",
  "capacity": 200,
  "imageUrl": "string",
  "eventType": "CONCERT",
  "ticketPrices": {
    "STANDARD": 50,
    "VIP": 100,
    "YOUNG": 30
}
```

## Documentation API
La documentation complète est disponible via **Swagger** :
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Collaboration
Projet developpé chez Technifutur en collaboration avec : 
- [Barnabé Dussart](https://github.com/AtomDus)
- [Daniel Hajdini](https://github.com/DanHajdini)

## Presentation
Visualisation des slides de presentation:
[Par Ici](https://docs.google.com/presentation/d/1oW73jsgTMPvrz-eO1IHosnfC_in_fSuCLFi3jHyT2sM/edit?usp=sharing)

