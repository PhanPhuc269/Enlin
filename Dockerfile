# --- Stage 1: Build (Cập nhật Maven chạy trên JDK 21) ---
# Dùng image maven kèm eclipse-temurin-21 để đảm bảo tương thích tốt nhất
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy các file cấu hình dependency trước để tận dụng Docker cache
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Cấp quyền thực thi cho file mvnw (quan trọng trên Linux/Docker)
RUN chmod +x mvnw

# Tải dependency về trước (Go offline)
RUN ./mvnw -B -f pom.xml dependency:go-offline -DskipTests

# Copy toàn bộ source code và build
COPY src ./src
RUN ./mvnw -B -DskipTests package

# --- Stage 2: Run (Cập nhật JRE 21) ---
# Dùng bản JRE 21 nhẹ và tối ưu
FROM eclipse-temurin:21-jre

# Tạo user không phải root để bảo mật (Security Best Practice)
RUN useradd -m appuser || true
USER appuser
WORKDIR /home/appuser

# --- QUAN TRỌNG: Sửa tên file cụ thể thành *.jar ---
# Lệnh này sẽ tự động lấy file jar bất kể tên là gì (enlin-0.0.1 hay enlin-1.0.0)
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

# Cấu hình RAM (Bạn có thể chỉnh lại tùy theo gói Free Tier)
# Ví dụ Render Free Tier chỉ có 512MB RAM thì nên set Xmx thấp chút (khoảng 350m-400m)
ENV JAVA_OPTS="-Xms256m -Xmx400m"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /home/appuser/app.jar"]