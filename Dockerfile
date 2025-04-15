# Etapa 1 - Build da aplicação
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Executa o build do projeto usando o Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Etapa 2 - Imagem final mais enxuta
FROM eclipse-temurin:21-jre

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=builder /app/target/produtosApi-*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8081

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]