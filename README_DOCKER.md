# Price Tracker - Docker Deployment

## Prérequis
- Docker & Docker Compose installés
- Accès à Internet pour cloner les repos Git

## Lancement rapide

### 1. Spécifier le repo du frontend

Modifiez le `docker-compose.yml` ou passez l'argument en build :

```bash
docker-compose build --build-arg FRONTEND_REPO_URL=https://github.com/your-org/price-tracker-frontend.git
```

### 2. Lancer l'application

```bash
docker-compose up
```

L'application sera accessible sur :
- **Frontend** : http://localhost:8080
- **Backend API** : http://localhost:8080/api
- **PostgreSQL** : localhost:5432

### 3. Arrêter l'application

```bash
docker-compose down
```

## Variables d'environnement

Vous pouvez surcharger les variables d'environnement :

```bash
docker-compose up \
  -e DB_URL=jdbc:postgresql://postgres:5432/pricetracker \
  -e DB_USER=pricetracker \
  -e DB_PASSWORD=pricetracker
```

## Build manuel

Si vous voulez build uniquement l'image Docker :

```bash
docker build \
  --build-arg FRONTEND_REPO_URL=https://github.com/your-org/price-tracker-frontend.git \
  -t price-tracker-api:latest .
```

Puis lancer avec PostgreSQL en arrière-plan :

```bash
# Lancer PostgreSQL
docker run -d \
  --name postgres \
  -e POSTGRES_DB=pricetracker \
  -e POSTGRES_USER=pricetracker \
  -e POSTGRES_PASSWORD=pricetracker \
  -p 5432:5432 \
  postgres:16-alpine

# Lancer l'app
docker run -d \
  --name price-tracker \
  -p 8080:8080 \
  --link postgres \
  -e DB_URL=jdbc:postgresql://postgres:5432/pricetracker \
  -e DB_USER=pricetracker \
  -e DB_PASSWORD=pricetracker \
  price-tracker-api:latest
```

## Architecture Docker

### Étape 1 : Build Frontend
- Clone du repo Git (spécifié via `FRONTEND_REPO_URL`)
- `npm ci` pour installer les dépendances
- `npm run build` pour générer le bundle

### Étape 2 : Build Backend
- Clone du code du backend
- Copie du bundle frontend dans `src/main/resources/static`
- `mvn clean package` pour compiler

### Étape 3 : Image finale
- JRE 25 légère
- JAR compilé
- Expose le port 8080
- Variables d'environnement pour la BDD

## Vérification de santé

PostgreSQL inclut un healthcheck. L'API attend 10s que PostgreSQL soit prêt.

```bash
docker-compose ps
```

## Dépannage

### L'app démarre mais ne peut pas se connecter à PostgreSQL

Vérifiez que PostgreSQL est bien démarré :
```bash
docker-compose logs postgres
```

### Le frontend ne charge pas

Vérifiez que le build a bien copié les fichiers :
```bash
docker exec price-tracker-api ls -la /app/src/main/resources/static
```

### Réinitialiser la base de données

```bash
docker-compose down -v  # Supprime aussi les volumes
docker-compose up        # Recrée la BDD depuis zéro
```

## Configuration JWT

La clé secrète JWT dans `application-docker.yml` est une valeur par défaut.
**À modifier en production !**

```yaml
app:
  jwt:
    secret: "VOTRE_CLE_SECRETE_DE_256_BITS_MINIMUM"
```
