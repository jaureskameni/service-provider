FROM eclipse-temurin:25-jre-alpine

RUN apk --no-cache add curl

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --chown=spring:spring cm.klg.service-provider/build/libs/*SNAPSHOT.jar app.jar

USER spring:spring

EXPOSE 8084

ENTRYPOINT ["java", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "--enable-preview", \
            "-jar", \
            "app.jar"]