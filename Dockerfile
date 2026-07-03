# Étape 1 : Récupération + build du front
FROM node:20-alpine AS frontend-build
ARG FRONTEND_REPO_URL=https://github.com/Tenalic/price-tracker.git
RUN apk add --no-cache git
RUN git clone --depth 1 ${FRONTEND_REPO_URL} /front
WORKDIR /front
RUN npm ci
RUN npm run build

# Étape 2 : Build du back
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/

# On copie le build React généré à l'étape 1 dans les ressources statiques
COPY --from=frontend-build /front/dist ./src/main/resources/static

RUN mvn clean package -DskipTests

# Étape 3 : Image finale légère
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
COPY --from=build /app/target/price-tracker-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
