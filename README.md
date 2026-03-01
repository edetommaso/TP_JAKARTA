# TP Master Annonce - Migration Spring Boot & K8s

Ce projet est la refonte complète de l'application Jakarta EE "Master Annonce" vers **Spring Boot 3**. Il met en œuvre des normes industrielles incluant Spring Data JPA, JWT Security, AOP Logging, Tests via Testcontainers, Docker et un déploiement Kubernetes local.

## Architecture & Organisation
- **API REST :** Les endpoints sont servis via des `@RestController` Spring.
- **Sécurité :** L'ancien système JAAS a été remplacé par un système `STATELESS` avec **Spring Security** et un filtre JWT (`JwtAuthenticationFilter`) validant le token signé par `jjwt`.
- **Accès aux données :** Implémenté via **Spring Data JPA**. La recherche par filtres multiples (auteur, statut, date) est résolue de manière complètement dynamique avec les **Specifications**. MapStruct assure le mapping DTO ↔ Entité pour un modèle de données imperméable.
- **Règles métier :** Les services incluent les règles de gestion (validation d'acteur). Ces règles sont vérifiées par un Logging Aspect central (`LoggingAspect`) et l'annotation `@PreAuthorize(hasRole)`.
- **Observabilité :** Documentation accessible sur `/swagger-ui/index.html` (OpenAPI v3) et vérifications de santé sur `/actuator/health`. Un `CorrelationId` est généré pour chaque requête via filtre et inséré dans les journaux (MDC).

## Choix Stratégique CI : Option 1 (Testcontainers)
Pour la base de données exécutée au moment de la CI (sur GitHub Actions), la **stratégie Testcontainers (Option 1)** a été choisie.
**Justification :** 
1. **Zéro configuration CI :** Aucune définition de "service PostgreSQL" archaïque n'est requise dans le YAML du workflow.
2. **Reproductibilité accrue :** Ce qui passe en local passera sur GitHub car c'est le **code Java lui-même** (le plugin Testcontainers / JUnit) qui monte la BDD dont le test a besoin.
3. **Parfaite isolation :** Chaque cycle de tests unitaires/intégration dispose d'un PostgreSQL tout neuf sur un port dynamique non conflictuels.

L'artefact final du pipeline s'appelle bien `master-annonce-jar`. Des plugins supplémentaires comme **JaCoCo** pour l'analyse de qualité et `docker build` (Bonus) sont exécutés localement par ce même workflow de façon continue sur `main`.

---

## Problèmes rencontrés
- **Instabilité globale du système.**
- **Conflits de versions majeurs.**
- **Erreurs de logique persistantes.**
- **Difficultés d'intégration technique.**
- **Comportements imprévisibles du moteur.**
- **Latences système inexpliquées.**
- **Pertes de données temporaires.**


---

## 🚀 Comment lancer le projet en local ?

Contrairement à Jakarta EE où il fallait déployer le `.war` dans un serveur d'application Tomcat externe, **Spring Boot embarque son propre serveur Tomcat !** 
Vous n'avez plus besoin d'installer Tomcat.

### Prérequis (Base de données)
Vous devez avoir une base de données PostgreSQL qui tourne sur le port `5432`.
Vous pouvez lancer uniquement la base via le fichier `docker-compose.yml` :
```bash
docker-compose up -d db
```

### Option 1 : Via votre IDE (Le plus simple)
Ouvrez votre projet dans IntelliJ IDEA, Eclipse ou VS Code.
Trouvez la classe `MasterAnnonceApplication.java` située dans `src/main/java/com/example/tpjakarta/`.
Faites un clic droit dessus et choisissez **Run 'MasterAnnonceApplication'**.

### Option 2 : En ligne de commande avec Maven
Depuis le dossier racine du projet (là où se trouve `pom.xml`), exécutez la cible `spring-boot:run` :
```bash
mvn spring-boot:run
```

### Option 3 : Tout via Docker Compose
Si vous voulez lancer à la fois la base de données et l'application en une seule commande, sans rien installer sur votre machine :
```bash
# Lancement de l'app et de DB
docker-compose up --build
```
L'application sera accessible sur `http://localhost:8080`.

---

## ⭐ SuperBonus: Déploiement Kubernetes MINIKUBE

Ce projet contient l'intégralité des YAMLs pour Kubernetes situés dans le dossier `/k8s`. 

### Prérequis Techniques
Installez Minikube puis exécutez les commandes pour activer l'ingress et configurer l'environnement Docker de Minikube.

```bash
minikube start
minikube addons enable ingress

# Sous Linux/Mac (pour que Docker construise l'image dans le registre Minikube)
eval $(minikube docker-env)

# Pour Windows Powershell (alternative) :
# minikube docker-env | Invoke-Expression
```

### 1) Builder l'image dans Minikube
*(Obligatoire pour que K8s trouve l'image sans registry public).*
```bash
docker build -t masterannonce:1.0 .
```

### 2) Lancer le réseau
Les manifests sont pré-configurés pour gérer les secrets en Base64, lier le ConfigMap et instancier **2 replicas** de l'Application pointant vers **postgres-service**.
```bash
kubectl apply -f k8s/
```

### 3) Vérifier le déploiement
```bash
# Vérifier si POSTGRES et MASTERANNONCE (x2 replicas) sont OK (READY 1/1)
kubectl get pods

# Vérifier les sondes de Liveness/Readiness
kubectl describe pod <nom-du-pod-app>
```

### 4) Exposer et Consommer l'API
Un Ingress a été configuré sur l'URL `masterannonce.local`.
*(Nécessite d'ajouter `masterannonce.local` pointant vers l'IP `minikube ip` dans votre fichier `hosts` système).*
```bash
# Variante via Minikube Tunnel si besoin :
minikube service masterannonce-service
```
L'application est 100% industrialisable selon la norme 12-Factor App (Configurations externalisées, stateless, sondes configurées).
