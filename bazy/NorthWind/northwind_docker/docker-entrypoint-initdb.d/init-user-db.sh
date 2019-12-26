#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    \i /docker-entrypoint-initdb.d/northwind.postgre.sql;
EOSQL
