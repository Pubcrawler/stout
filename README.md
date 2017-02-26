# Stout #

REST API for pubcrawler.

## Build & Run ##

```sh
$ ./sbt
> jetty:start
```

REST API is then available on [http://localhost:8080/](http://localhost:8080/).


# Development Setup

## Docker installation and running local postgres docker container

1. ```$ brew install docker```
2. ```$ docker pull postgres```
3. ```$ docker run -p 127.0.0.1:{PORT}:5432 --name stout -e POSTGRES_PASSWORD=password -d postgres```
4. Add the following to your environment variables:
    - ```DOCKER_HOST=unix:///var/run/docker.sock```
    - ```DB_URL=jdbc:postgresql://127.0.0.1:{PORT}/postgres``` # F
    - ```DB_USER=postgres```
    - ```DB_PASS=password```

Remember to run Docker-application on macOS and enable service on Linux.

For windows installation, visit [https://docs.docker.com/docker-for-windows/](https://docs.docker.com/docker-for-windows/).

More details on the postgres docker container and configuration options, visit [https://hub.docker.com/_/postgres/](https://hub.docker.com/_/postgres/).


## Using flyway

Use ```$ sbt flywayClean``` to clean your database
Use ```$ sbt flywayMigrate``` to migrate your database

## Debugging purposes
Should you need it, you can go directly into the docker image to see whether or not your code behaves the way you want it.
```
$ docker exec -it {name} sh
# su postgres
# psql
# \c
# select * from users;
```
