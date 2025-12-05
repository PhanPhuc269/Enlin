# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Chỉ copy pom.xml (Không cần copy mvnw hay .mvn nữa)
COPY pom.xml .

# 2. Dùng lệnh 'mvn' gốc của image thay vì './mvnw'
# Lệnh này tải các thư viện về cache
RUN mvn dependency:go-offline -DskipTests

# 3. Copy source code
COPY src ./src

# 4. Build dự án bằng lệnh 'mvn'
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre

RUN useradd -m appuser || true
USER appuser
WORKDIR /home/appuser

# Copy file jar (Dùng *.jar để tự động bắt tên)
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

# Cấu hình RAM cho Render (Giữ nguyên như cũ)
ENV JAVA_OPTS="-Xms256m -Xmx400m"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /home/appuser/app.jar"]