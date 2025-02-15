#!/bin/bash

# This file is work-in-progress, doesn't work well and it's probably just a bad
# idea.  We should do a separate export from prod where we avoid dumping the 
# app-user table.  Even that will still have issues with service points though.
# Maybe it's just time to have a separate TEST env and let DEMO be more stable.

set -o errexit

echo "inside restore container"
pwd
ls -la

# this is the env var that psql will pick up from 
export PGPASSWORD="${PG_ADMIN_PASSWORD:?PG_ADMIN_PASSWORD environment variable is empty}"

PG_DUMP_FILE="import-data/${PG_DUMP_FILE:?PG_DUMP_FILE environment variable is empty}"

#make sure the file exists
ls -la $PG_DUMP_FILE

#export PGPASSWORD=$PG_ADMIN_PASSWORD:?PG_ADMIN_PASSWORD environment variable is empty

export HOST_PORT="--host=$PG_HOST --port=$PG_PORT"
export FAIL_ON_ERROR="-v ON_ERROR_STOP=1"

echo "show PG version on $HOST_PORT"
psql $HOST_PORT --username=$PG_ADMIN_USER --file select-version.sql

psql $HOST_PORT $FAIL_ON_ERROR --username=$PG_ADMIN_USER \
  --dbname $RAIDO_DB_NAME \
  -v STASH_SCHEMA_NAME="demo_stash" \
  -v RAIDO_DB_NAME="$RAIDO_DB_NAME" \
  -v API_SCHEMA_NAME="$API_SCHEMA_NAME" \
  --file stash-app-user-data.sql


echo "kick users off of $RAIDO_DB_NAME"
#cat kick-user.sql
psql $HOST_PORT --username=$PG_ADMIN_USER \
  -v RAIDO_DB_NAME="$RAIDO_DB_NAME" \
  --file kick-user.sql

echo "restoring into database $RAIDO_DB_NAME"
pg_restore $HOST_PORT \
  --clean \
  --verbose --exit-on-error --single-transaction --no-owner \
  --username=$PG_ADMIN_USER --dbname=$RAIDO_DB_NAME   \
  --schema=$API_SCHEMA_NAME \
  $PG_DUMP_FILE

psql $HOST_PORT --username=$PG_ADMIN_USER \
  --dbname $RAIDO_DB_NAME \
  -v STASH_SCHEMA_NAME="demo_stash" \
  -v RAIDO_DB_NAME="$RAIDO_DB_NAME" \
  -v API_SCHEMA_NAME="$API_SCHEMA_NAME" \
  --file bring-stashed-app-user-data.sql

