FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY orderEntity-service/build/libs/OrderService-0.0.1-SNAPSHOT.jar ./OrderService.jar
COPY inventory-service/build/libs/InventoryService-0.0.1-SNAPSHOT.jar ./InventoryService.jar
COPY notification-service/build/libs/NotificationService-0.0.1-SNAPSHOT.jar ./NotificationService.jar

COPY ../start-services.sh ./start-services.sh
RUN chmod +x ./start-services.sh

EXPOSE 8085 8086 8087

CMD ["./start-services.sh"]