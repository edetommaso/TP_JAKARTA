# Projet MasterAnnonce - TP Jakarta EE

Ce projet est une refactorisation d'une application web Java EE initialement basée sur Servlets/JSP et JDBC. L'objectif de ce TP était de moderniser l'application en remplaçant la couche d'accès aux données par JPA/Hibernate et en structurant le code de manière plus robuste et maintenable.

## Architecture et Fonctionnalités

L'application permet de gérer des annonces, des catégories et des utilisateurs.

- **Modèle de données (Beans)** : Les entités `Annonce`, `Category`, et `User` sont mappées avec JPA.
- **Couche d'accès aux données (Repositories)** : Le pattern DAO a été remplacé par une couche Repository utilisant JPA (`EntityManager`) pour toutes les opérations CRUD.
- **Couche Service** : Une couche de service a été introduite pour contenir la logique métier (ex: publication d'une annonce).
- **Couche Web (Servlets & JSP)** : L'interface utilisateur est gérée par des Servlets qui communiquent avec les repositories et des pages JSP qui utilisent JSTL pour l'affichage.
- **Sécurité** : Un système d'authentification basé sur les sessions et un filtre de sécurité (`SecurityFilter`) protègent les ressources de l'application.

### Fonctionnalités implémentées :
- CRUD complet pour les Annonces, Catégories et Utilisateurs.
- Système de connexion/déconnexion et d'inscription.
- Un utilisateur ne peut modifier ou supprimer que ses propres annonces.
- Seules les annonces avec le statut `PUBLISHED` sont visibles par tous. Les autres ne sont visibles que par leur auteur.
- Base de données H2 persistante sur fichier, avec mise à jour automatique du schéma.

## Défi d'Environnement de Développement

### Problème Rencontré

Le développement de ce projet a été réalisé sur deux environnements distincts : le poste de l'IUT et un ordinateur personnel. Les configurations de bases de données entre ces deux machines n'étaient pas identiques (présence et configuration d'un serveur PostgreSQL sur une machine mais pas sur l'autre).

Cette différence d'environnement a entraîné des difficultés et des erreurs récurrentes lors de l'initialisation de la connexion à la base de données (`PersistenceException`). Il était complexe de maintenir un workflow de développement fluide et de garantir que l'application puisse démarrer de manière fiable sur les deux postes.

### Solution Adoptée

Pour résoudre ce problème de portabilité et de consistance, la décision a été prise d'abandonner la dépendance à un serveur de base de données externe comme PostgreSQL au profit d'une **base de données H2 en mode fichier**.

Cette solution présente plusieurs avantages :
- **Portabilité** : La base de données est contenue dans un fichier directement au sein du projet. Aucune installation ni configuration externe n'est requise.
- **Simplicité** : Il suffit de lancer l'application pour que la base de données soit immédiatement disponible.
- **Consistance** : L'environnement de base de données est strictement identique sur toutes les machines où le projet est cloné, éliminant les erreurs d'initialisation liées à la configuration.

Ce choix a rendu le développement beaucoup plus simple et fiable entre les différents environnements.
