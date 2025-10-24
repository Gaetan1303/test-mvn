# ----------------------------------------------------------------------------------
# ÉTAPE 1 : BUILD - Utilise une image JDK complète pour compiler le code
# Utiliser l'image Maven ou Gradle selon votre choix final
# Si vous utilisez Maven (pom.xml) :
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

# Si vous utilisez Gradle (build.gradle) :
# FROM gradle:8.5.0-jdk21-alpine AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers de définition de build pour le téléchargement des dépendances
# Cela permet au build de cache les dépendances si le code source ne change pas.
COPY pom.xml .
COPY build.gradle .
COPY settings.gradle .

# Télécharger les dépendances
# Si Maven :
RUN mvn dependency:go-offline

# Si Gradle :
# RUN gradle dependencies

# Copier le code source complet
COPY src /app/src

# Construire le JAR exécutable (le fichier .jar)
RUN mvn clean package -DskipTests

# Si Gradle :
# RUN gradle clean build -x test

# ----------------------------------------------------------------------------------
# ÉTAPE 2 : PRODUCTION - Utilise une image JRE légère pour l'exécution
FROM eclipse-temurin:21-jre-alpine

# Définir le répertoire de travail
WORKDIR /app

# Le nom du fichier JAR généré. Spring Boot génère généralement : <artifact-id>-<version>.jar
# Adaptez le nom du JAR si nécessaire.
ARG JAR_FILE=target/test-0.0.1-SNAPSHOT.jar

# Copier le JAR construit depuis l'étape 'build' vers l'étape 'production'
COPY --from=build /app/${JAR_FILE} app.jar

# Définir le port que l'application utilise (par défaut Spring Boot est 8080)
EXPOSE 8080

# Commande pour démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]