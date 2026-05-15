# GIAI ĐOẠN 1: Build file JAR từ mã nguồn
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
# Copy file cấu hình Maven và mã nguồn vào
COPY pom.xml .
COPY src ./src
# Thực hiện build dự án (bỏ qua chạy test để nhanh hơn)
RUN mvn clean package -DskipTests

# GIAI ĐOẠN 2: Tạo Image chạy ứng dụng (siêu nhẹ)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Chỉ lấy file JAR vừa build xong từ Giai đoạn 1 sang
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]