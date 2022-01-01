#!/bin/bash

# This script should be run from inside a Docker container

# use the -D flag to set the JDBC_DATABASE_URL environment variable and pass it to the java web application to be able
# to connect to the database
#
# NOTE: `postgres-db-container` is the name of the container running the postgres image and is also the name of the host computer
# to which the web application will connect
java -DJDBC_DATABASE_URL="jdbc:postgresql://postgres-db-container:5432/postgresondockerdb?user=postgresondockeruser&password=postgresondockerpassword" -jar target/java-getting-started-1.0.jar
