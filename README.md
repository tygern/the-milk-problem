# The Milk Problem

## Configuration

### Quick start

The below sections walk through all the environment variables necessary to
run the application in both local and production environments.

Feel free to create a .env_development file for local development with the below variables -

### Development example

.env_development

```bash
export PORT=8881

export JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/milk_development
export JDBC_DATABASE_USERNAME=milk
export JDBC_DATABASE_PASSWORD=milk
```

### Test example

.env_test

```bash
export PORT=8881

export JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/milk_test
export JDBC_DATABASE_USERNAME=milk
export JDBC_DATABASE_PASSWORD=milk
```

### Postgresql

```bash
brew install postgresql
brew services run postgres
```

### Testing setup

Create the _milk_ databases.

```bash
createdb
psql -c "create database milk_test;"
psql -c "create user milk with password 'milk';"
psql --username=milk --password -h localhost milk_test
```

#### Migrate the databases.

```bash
flyway -user=milk -password=milk -url="jdbc:postgresql://localhost:5432/milk_test" -locations=filesystem:databases/milk clean migrate
```


### Server

Configure the port the delivery server runs on.

```bash
export PORT=8881
```

## Development

### Milk

```bash
psql -c "create database milk_development;"
```

```bash
flyway -user=milk -password=milk -url="jdbc:postgresql://localhost:5432/milk_development" -locations=filesystem:databases/milk clean migrate
```

### Data

Load a product scenario
```bash
psql -f applications/products-server/src/test/resources/scenarios/products.sql milk_development
```
