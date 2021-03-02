# The Milk Problem

An example architecture used for managing product inventory.

The application highlights the use of database transactions.

### History

The milk problem first surfaced while working with a well known grocery store to track product inventory in real-time.
The initial solution leveraged an eventually consistent database - or an available and partition-tolerant database.
The choice of database was largely driven by a non-trivial performance requirement.

The challenge is that availability has a cost - and the eventually consistent database is notorious for
serving dirty data - or allowing uncommitted changes from one transaction to affect a read in another
transaction (aka "dirty read").

The below exercise introduces the reader to transactions while highlighting the challenges with dirty reads.

## Quick start

The below sections walk through all the environment variables necessary to
run the application in both local and production environments.

### Install Postgresql

```bash
brew install postgresql
brew services run postgres
```

### Create the postgresql database.

```bash
createdb
```

## Testing setup

Create the _milk_ databases.

```bash
psql -c "create database milk_test;"
psql -c "create user milk with password 'milk';"
```

### Migrate the databases.

```bash
flyway -user=milk -password=milk -url="jdbc:postgresql://localhost:5432/milk_test" -locations=filesystem:databases/milk clean migrate
```

### Local test environment.

Create a .env_test file for local testing with the below variables -

```bash
export PORT=8881

export JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/milk_test
export JDBC_DATABASE_USERNAME=milk
export JDBC_DATABASE_PASSWORD=milk
```

## Development setup

Create the _milk_ databases.

```bash
psql -c "create database milk_development;"
```

### Migrate the databases.

```bash
flyway -user=milk -password=milk -url="jdbc:postgresql://localhost:5432/milk_development" -locations=filesystem:databases/milk clean migrate
```

### Local development environment.

Create a .env_development file for local work with the below variables -

```bash
export PORT=8881

export JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/milk_development
export JDBC_DATABASE_USERNAME=milk
export JDBC_DATABASE_PASSWORD=milk
```

### Initial data

Load a product scenario.

```bash
psql -f applications/products-server/src/test/resources/scenarios/products.sql milk_development
```
