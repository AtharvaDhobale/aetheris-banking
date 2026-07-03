# Local Database Setup Guide

## PostgreSQL Setup

### Windows
1. Download PostgreSQL from https://www.postgresql.org/download/windows/
2. Run the installer and follow the installation wizard
3. Remember the password you set for the `postgres` user
4. During installation, keep the default port 5432
5. After installation, open pgAdmin or use Command Prompt:
   ```cmd
   psql -U postgres
   ```
6. Create the database:
   ```sql
   CREATE DATABASE accounting_db;
   ```

### macOS
```bash
brew install postgresql
brew services start postgresql
createdb accounting_db
```

### Linux (Ubuntu/Debian)
```bash
sudo apt-get install postgresql postgresql-contrib
sudo -u postgres createdb accounting_db
```

## MongoDB Setup

### Windows
1. Download MongoDB from https://www.mongodb.com/try/download/community
2. Run the installer and follow wizard
3. MongoDB will run as a Windows Service by default on localhost:27017

### macOS
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

### Linux (Ubuntu/Debian)
```bash
sudo apt-get install -y mongodb
sudo systemctl start mongodb
```

## Verify Setup

### PostgreSQL
```bash
psql -U postgres -d accounting_db -c "SELECT version();"
```

### MongoDB
```bash
mongosh
> db.adminCommand("ping")
```

## Update Application Configuration

In `src/main/resources/application.yml`:
- PostgreSQL user: postgres (default)
- PostgreSQL password: (password from installation)
- PostgreSQL database: accounting_db
- MongoDB: mongodb://localhost:27017/accounting_logs

## Run Application

```bash
cd c:\Users\athar\OneDrive\Desktop\AM
mvn clean package
mvn spring-boot:run
```

Access: http://localhost:8080/login
