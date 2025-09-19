# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Crea el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todo el proyecto al contenedor
COPY . .

# Ejecuta la build del proyecto sin correr los tests
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-focal

# Crea un usuario no-root por seguridad
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Crea directorio de trabajo
WORKDIR /app

# Copia el JAR desde el builder
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando por defecto al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]
