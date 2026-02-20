# TP Jakarta EE - Gestion d'Annonces

Ce projet est une application web RESTful réalisée dans le cadre du TP Jakarta EE. Elle permet la gestion d'annonces (CRUD, publication) avec une couche de sécurité et des règles métier avancées.

## Fonctionnalités

- **API REST** avec JAX-RS (Jersey).
- **Persistance** des données avec JPA (Hibernate)
- **Sécurité Avancée** :
  - Authentification par Token (Stateless).
  - **Bonus JAAS** : Implémentation complète avec `LoginModule` custom (`DbLoginModule`, `TokenLoginModule`) et `Principals`.
  - Contrôle d'accès bas sur les rôles (Auteur uniquement).
  - Architecture sécurisée : Filtres JAX-RS + LoginContext JAAS.
- **Règles métier** :
  - Archivage obligatoire avant suppression (`DELETE` bloqué si non `ARCHIVED`).
  - Immutabilité des annonces publiées (`UPDATE` bloqué si `PUBLISHED`).
  - Gestion de la concurrence (`@Version`).
- **Documentation API** : OpenAPI 3.0 / Swagger.
- **Tests** :
  - Unitaires (Mockito).
  - Intégration (Jersey Test, H2 In-Memory).
  - **Charge** : Plan de test JMeter fourni (`tests/load/load-test.jmx`).
- **CI/CD** : Pipeline GitHub Actions.

## Exercices Réalisés

1.  [x] Configuration JAX-RS.
2.  [x] API REST Annonce (CRUD).
3.  [x] Validation (Bean Validation).
4.  [x] Gestion des Erreurs.
5.  [x] Authentification Stateless (**+ Bonus JAAS**).
6.  [x] Sécurité (`AuthenticationFilter` + JAAS).
7.  [x] Règles Métier Avancées.
8.  [x] Tests Unitaires.
9.  [x] Tests d'Intégration.
10. [x] Industrialisation (Swagger, CI/CD, **Load Test**).

## Prérequis

- Java 21+
- Maven 3.8+
- Docker (optionnel pour PostgreSQL si configuré, ici H2 par défaut).

## Démarrage

### 1. Cloner et Compiler
```bash
./mvnw clean package
```

### 2. Lancer les Tests
```bash
./mvnw test
```

### 3. Démarrer le Serveur (Tomcat via Cargo ou Plugin Maven)
Si vous utilisez le plugin maven Tomcat (si configuré) ou déployez le WAR généré dans un Tomcat externe.
*Note : Le projet génère un WAR dans `target/tp-jakarta-1.0-SNAPSHOT.war`.*

### 4. Accéder à l'API
- Base URL : `http://localhost:8080/tp-jakarta/api`
- Swagger OpenAPI : `http://localhost:8080/tp-jakarta/api/openapi.json`

## Endpoints Principaux

| Méthode | URI | Description | Auth Requise |
|---|---|---|---|
| POST | `/api/login` | S'authentifier (Username/Password) | Non |
| GET | `/api/annonces` | Lister les annonces | Non (ou Oui selon config) |
| POST | `/api/annonces` | Créer une annonce | Oui |
| PUT | `/api/annonces/{id}` | Modifier une annonce | Oui (Auteur) |
| DELETE | `/api/annonces/{id}` | Supprimer une annonce (doit être archivée) | Oui (Auteur) |
| POST | `/api/annonces/{id}/archive` | Archiver une annonce | Oui (Auteur) |


## Problèmes Rencontrés & Solutions

1.  **Configuration Swagger & Context Path**
    *   **Problème** : Erreur 404 lors des appels API depuis Swagger UI due à un chemin de contexte incorrect.
    *   **Solution** : Configuration du déploiement à la racine (`<finalName>ROOT</finalName>`) et définition explicite de l'URL du serveur dans `@OpenAPIDefinition`.

2.  **Authentification Swagger**
    *   **Problème** : Impossibilité de tester les routes sécurisées via l'interface Swagger par défaut.
    *   **Solution** : Ajout de la configuration `@SecurityScheme` (Type HTTP, Scheme Bearer, Format JWT) pour activer le bouton "Authorize".

3.  **Intégration JAAS & JWT**
    *   **Problème** : Difficulté à concilier l'authentification standard JAAS avec une architecture REST Stateless (Token).
    *   **Solution** : Développement de modules JAAS personnalisés (`DbLoginModule` pour le login initial, `TokenLoginModule` pour la validation du token à chaque requête).

4.  **Filtrage de Sécurité**
    *   **Problème** : Le filtre de sécurité bloquait ou autorisait incorrectement certaines ressources.
    *   **Solution** : Mise en place d'une liste blanche stricte pour les routes publiques (`/auth`, `/openapi`, `GET /annonces`) et validation des rôles via les `Principals` du `Subject` JAAS.
