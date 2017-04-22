#!/usr/bin/env bash
docker pull postgres
docker kill stout-postgres
docker container rm stout-postgres
docker run --name stout-postgres -p 127.0.0.1:5432:5432 -e POSTGRES_USER=stout -e POSTGRES_PASSWORD=stout -d postgres
