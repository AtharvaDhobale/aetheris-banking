@echo off
REM Start PostgreSQL and MongoDB using Docker Compose
docker-compose up -d

REM Wait for services to be healthy
echo Waiting for PostgreSQL to be ready...
timeout /t 10 /nobreak

echo Waiting for MongoDB to be ready...
timeout /t 5 /nobreak

echo Databases are running!
echo PostgreSQL: localhost:5432/accounting_db (user: postgres, password: postgres)
echo MongoDB: localhost:27017/accounting_logs
