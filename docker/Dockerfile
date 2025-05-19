FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ../module-service1/build/libs/service1.jar ./service1.jar
COPY ../module-service2/build/libs/service2.jar ./service2.jar
COPY ../module-service3/build/libs/service3.jar ./service3.jar

COPY start-services.sh ./start-services.sh
RUN chmod +x ./start-services.sh

EXPOSE 8085 8086 8087

CMD ["./start-services.sh"]