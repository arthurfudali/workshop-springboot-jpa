# Build
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas o pom.xml e baixa dependências antes do código-fonte
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copia o restante do código-fonte e faz o build
COPY src ./src
RUN mvn clean package -DskipTests -B


# Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o .jar gerado
COPY --from=build /app/target/*.jar app.jar

# Define um usuário não root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Define variáveis de ambiente para otimização do runtime
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
