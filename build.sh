#!/bin/bash

SCRIPT_DIR=$(dirname "$0")

echo "Building Spring Boot application with Gradle..."
./gradlew :module-interface:clean &&
./gradlew :module-infrastructure:persistence-database:test &&
./gradlew :module-domain:test &&
./gradlew :module-interface:build

echo "Docker image build and starting containers with Docker Compose..."
cd "$SCRIPT_DIR"
docker-compose up --build -d

echo "Build and deployment process completed successfully."
