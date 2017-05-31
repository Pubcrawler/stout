# Stout #

GraphQL API for pubcrawler.

## Installation
To run Stout, you need `sbt`.

For OSX, run:
```sh
$ brew install sbt
```

For Windows, read the instructions on [http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Windows.html](http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Windows.html).

## Database setup
Stout requires a running PostgreSQL database. By default, Stout assumes that
the server runs on `localhost:5432`, with a database named `stout`, a user
`stout`, and a password `stout`  password. These connection parameters are set in 
`application.conf`, and can be overridden by setting the following environment variables:

- `POSTGRES_JDBC_URL`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`

To set up a PostgreSQL server with the default settings, run the `bin/start-postgres.sh`
script.

## Database migrations
To migrate the database, run the following command in SBT:
```
> flywayMigrate
```
To clean old contents of the database, run:
```
> flywayClean
```


## Build & Run ##

```sh
$ ./sbt
> jetty:start
```

For live reloading, subtsitute `jetty:start` with `~;jetty:stop;jetty:start`.

GraphQL API is then available on [http://localhost:8080/](http://localhost:8080/).

## API documentation
We have generated swagger documentation for our API. The swagger documentation is availble at [http://localhost:8080/docs](http://localhost:8080/docs). Swagger-ui enables you to view the documentation as well as live testing. Head over to [https://github.com/swagger-api/swagger-ui/](https://github.com/swagger-api/swagger-ui/).

It is to be noted that since we currently support v1.2 of the swagger specification, only swagger-ui of version v2.x is supported.

## Testing
```sh
$ ./sbt
> test
```

## Debugging in IntelliJ IDEA
Start SBT and the container as usual (`./sbt` then `jetty:start`).

After that, go to `Run`-> `Edit configurations` in IntelliJ. Click the `+`
button, select `Remote` to make a new remote debugging configuration, and
call it Scalatra Debug. In IntelliJ 15, the default run conf should work (it
looks like this):

```
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
```
Now just select `Run` -> `Debug 'Scalatra Debug'`. Setting breakpoints and 
stepping through code should work.

## Building for deployment
You can build a standalone jetty servlet for deployment by using `sbt-assembly` plugin like this:
```
$ sbt assembly
```
You can then run the server using:
```
$ java -jar target/scala-2.12/Stout-assembly-0.0.1-SNAPSHOT.jar
```

## Development Setup

### Docker installation and running local postgres docker container

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


### Using flyway

Use ```$ sbt flywayClean``` to clean your database
Use ```$ sbt flywayMigrate``` to migrate your database

### Debugging purposes
Should you need it, you can go directly into the docker image to see whether or not your code behaves the way you want it.
```
$ docker exec -it {name} sh
# su postgres
# psql
# \c
# select * from users;
```