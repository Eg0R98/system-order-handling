#!/bin/bash

java -jar OrderService.jar &
java -jar InventoryService.jar &
java -jar NotificationService.jar &

wait