# Stout #

REST API for pubcrawler.

## Build & Run ##

```sh
$ ./sbt
> jetty:start
```

REST API is then available on [http://localhost:8080/](http://localhost:8080/).


# Development Setup

## Postgres

1. Install docker
2. ```$ docker pull postgres```
3. ```$ docker run -p 127.0.0.1:5432:5432 --name stout -e POSTGRES_PASSWORD=password -d postgres```
4. Set ```DOCKER_HOST=unix:///var/run/docker.sock``` to your environment variables.

Hopefully, we something more automatic by the end of this PR.
Resource: [https://hub.docker.com/_/postgres/](https://hub.docker.com/_/postgres/)
