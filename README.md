# Migrating from one PostgreSQL to another via S3

In one terminal start initial setup.
```
./gradlew dockerBuildImage
docker-compose up --build dbmig-s3 dbmig-db1 dbmig-db2 dbmig-app1 dbmig-dumper
```
This will create data via the first spring boot app and dump it to S3 and then restore it to the second DB.

In another terminal start the second spring boot app.
```
docker-compose up --build dbmig-app2
```

Compare the two states.
```
curl -s -X GET http://localhost:8081/all | md5sum
curl -s -X GET http://localhost:8082/all | md5sum
```
