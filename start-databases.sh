#!/bin/bash
# Start PostgreSQL and MongoDB using Docker Compose
docker-compose up -d

# Wait for services to be healthy
echo "Waiting for PostgreSQL to be ready..."
sleep 10

echo "Waiting for MongoDB to be ready..."
sleep 5

echo "Databases are running!"
echo "PostgreSQL: localhost:5432/accounting_db (user: postgres, password: postgres)"
echo "MongoDB: localhost:27017/accounting_logs"
