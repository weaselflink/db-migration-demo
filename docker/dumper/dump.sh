#!/bin/bash

sleep 7

pwd
pg_dump --version
pg_restore --version
AWS_COMMAND="aws --no-sign-request --endpoint-url=$AWS_ENDPOINT"
$AWS_COMMAND --version

curl -s -X POST "http://dbmig-app1:8081/create/100"

$AWS_COMMAND s3api create-bucket --bucket $DUMP_BUCKET

TIMESTAMP=`date +"%FT%T"`

# hostname:port:database:username:password
echo "$DUMPED_HOST:$DUMPED_PORT:$DUMPED_DB:$DUMPED_USER:$DUMPED_PASSWORD" > $HOME/.pgpass
echo "$RESTORED_HOST:$RESTORED_PORT:$RESTORED_DB:$RESTORED_USER:$RESTORED_PASSWORD" >> $HOME/.pgpass
chmod 600 $HOME/.pgpass
#cat $HOME/.pgpass

pg_dump \
    --blobs \
    --format=custom \
    --no-owner \
    --no-privileges \
    --no-acl \
    --host=$DUMPED_HOST \
    --port=$DUMPED_PORT \
    --username=$DUMPED_USER \
    $DUMPED_DB \
    | $AWS_COMMAND s3 cp - "s3://$DUMP_BUCKET/dump_${TIMESTAMP}.dump"

$AWS_COMMAND s3 ls "s3://$DUMP_BUCKET"

$AWS_COMMAND s3 cp "s3://$DUMP_BUCKET/dump_${TIMESTAMP}.dump" - \
    | pg_restore \
    --dbname=$RESTORED_DB \
    --format=custom \
    --no-owner \
    --no-privileges \
    --no-acl \
    --host=$RESTORED_HOST \
    --port=$RESTORED_PORT \
    --username=$RESTORED_USER
