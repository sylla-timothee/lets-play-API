# lets-play-API
Projet : Let's Play – API REST CRUD avec Spring Boot & MongoDB
Dans ce projet, vous allez construire une API REST CRUD nommée Let's Play, en utilisant Spring Boot et MongoDB. Le système gérera des utilisateurs et des produits, permettant des opérations de création, lecture, mise à jour et suppression (CRUD) sur ces deux entités.

Vous implémenterez également l'authentification et l'autorisation via une sécurité basée sur les tokens, garantissant que seuls les utilisateurs autorisés peuvent effectuer des actions restreintes. Ce projet se concentre sur les meilleures pratiques du développement backend : codage sécurisé, gestion des erreurs, principes de conception REST et gestion des accès par rôles.

Mise en situation (Role Play)
Vous êtes un développeur backend chargé de concevoir une API REST sécurisée et évolutive pour une petite plateforme de type e-commerce. Votre objectif est de créer une application qui permet aux administrateurs de gérer tous les utilisateurs et produits, tandis que les utilisateurs standards ne peuvent gérer que leurs propres produits. Le système doit être sécurisé, robuste et pleinement conforme aux standards REST.

Objectifs d'apprentissage
Maîtriser Spring Boot et la conception d'API RESTful.

Intégrer et gérer des données avec MongoDB.

Implémenter des opérations CRUD pour plusieurs entités.

Appliquer Spring Security et l'authentification JWT.

Gérer le contrôle d'accès basé sur les rôles (Admin vs Utilisateur).

Implémenter la gestion sécurisée des mots de passe (hachage et salage).

Mettre en place une gestion d'erreurs robuste avec des réponses HTTP explicites.

1. Conception de la base de données
Concevez deux entités principales : User (Utilisateur) et Product (Produit), avec une relation "un-à-plusieurs" (un utilisateur peut posséder plusieurs produits).

classDiagram
    User "1" -- "n" Product : Possède
    User : +String id
    User : +String name
    User : +String email
    User : +String password
    User : +String role
    Product : +String id
    Product : +String name
    Product : +String description
    Product : +Double price
    Product : +String userId


2. Développement de l'API
Construisez des API RESTful pour les utilisateurs et les produits en respectant les méthodes HTTP et les codes de réponse appropriés. Implémentez les points d'accès (endpoints) suivants :

GET /products → Accès public (aucune authentification requise).

POST /products → Créer un nouveau produit (utilisateurs authentifiés uniquement).

PUT /products/{id} et DELETE /products/{id} → Limité aux propriétaires du produit ou aux admins.

GET /users et endpoints associés → Accessibles aux administrateurs uniquement.

3. Authentification et Autorisation
Implémentez l'authentification JWT avec Spring Security.

Permettez aux utilisateurs de s'inscrire, de se connecter et de recevoir des tokens.

Restreignez l'accès selon les rôles :

Admin : Gérer tous les utilisateurs et tous les produits.

User : Gérer uniquement ses propres produits.

4. Gestion des erreurs
Assurez-vous que l'API ne renvoie jamais d'erreurs 5XX non gérées.

Utilisez une gestion globale des exceptions pour capturer et formater les réponses d'erreur.

Renvoyez des codes d'état HTTP clairs (400, 401, 403, 404, 409, etc.).

5. Mesures de sécurité
Hachez et salez les mots de passe avec BCrypt avant la sauvegarde.

Validez et assainissez (sanitize) les entrées utilisateur pour prévenir les injections MongoDB.

Excluez les champs sensibles (ex: mot de passe) des réponses de l'API.

Utilisez HTTPS pour la transmission sécurisée des données.

Contraintes
Utiliser Spring Boot et MongoDB (pas de bases de données SQL).

Utiliser Spring Security ou JWT pour l'authentification.

Retourner toutes les données au format JSON.

Aucune donnée sensible ne doit apparaître dans les réponses de l'API.

Évaluation
Le projet sera évalué par une revue de code et des tests fonctionnels selon les critères suivants :

⚙️ Fonctionnalité : CRUD et authentification correctement implémentés.

🔐 Sécurité : Gestion correcte des rôles et des mots de passe.

🚫 Gestion des erreurs : Codes d'état appropriés et aucune erreur 5XX brute.

🧱 Qualité du code : Code propre, modulaire et bien structuré.

📘 Documentation : Instructions claires et explication des endpoints.

Fonctionnalités Bonus (Optionnel)
Configuration CORS : Implémenter des politiques de partage de ressources cross-origin précises.

Limitation de débit (Rate Limiting) : Prévenir les attaques par force brute ou les requêtes excessives.
