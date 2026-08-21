# Dùng JAR đã được build sẵn trên host, không cần Maven trong Docker
# Tránh lỗi SSL khi Docker container không thể kết nối Maven Central
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]