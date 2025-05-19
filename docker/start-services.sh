#!/bin/bash

java -jar OrderService-0.0.1-SNAPSHOT.jar &
java -jar InventoryService-0.0.1-SNAPSHOT.jar &
java -jar NotificationService-0.0.1-SNAPSHOT.jar &

wait