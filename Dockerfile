FROM node:20-alpine AS frontend-builder
WORKDIR /frontend
COPY collegeWithAi_frontend/package*.json ./
RUN npm install
COPY collegeWithAi_frontend/ ./
ENV VITE_API_BASE_URL=/api/v1
RUN npm run build

FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-builder
WORKDIR /backend
COPY collegeWithAI/pom.xml .
RUN mvn dependency:go-offline -B
COPY collegeWithAI/src ./src

COPY --from=frontend-builder /frontend/dist/ ./src/main/resources/static/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-builder /backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
