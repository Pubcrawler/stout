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
`application.conf`, and can be overridden by setting the following environment variable:

- `DATABASE_JDBC_URL`

To set up a PostgreSQL server with the default settings, run the `bin/start-postgres.sh`
script.

## Database migrations
To migrate the database, run the following command
```
$ ./sbt flywayMigrate
```
To clean old contents of the database, run:
```
$ ./sbt flywayClean
```


## Build & Run ##

```sh
$ ./sbt
> jetty:start
```

For live reloading, subtsitute `jetty:start` with `~;jetty:stop;jetty:start`.

GraphQL API is then available on [http://localhost:8080/](http://localhost:8080/).

## Testing
```sh
$ ./sbt test
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

