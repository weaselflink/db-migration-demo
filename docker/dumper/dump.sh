#!/bin/bash

sleep 5

pwd
echo $DUMP_BUCKET

pg_dump --version
echo $POSTGRES_PASSWORD \
    | pg_dump --blobs \
    --format=plain \
    --no-privileges \
    --no-acl \
    --host=$POSTGRES_HOST \
    --port=$POSTGRES_PORT \
    --username=$POSTGRES_USER \
    --password \
    $POSTGRES_DB
