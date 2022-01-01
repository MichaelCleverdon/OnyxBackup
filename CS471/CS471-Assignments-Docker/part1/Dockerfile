# This file contains a few small bugs that you will fix according to the assignment instructions

# Start with a Ubuntu LTS (https://wiki.ubuntu.com/LTS) base image (i.e., the one from April 2018),
# to increase the reproducibility of the build.
# For example, building this Docker image using the latest version of ubuntu may fail,
# because the start image in this Dockerfile keeps changing, and may break dependencies or
# introduce non-backward compatible changes
FROM ubuntu:18.04

# update available package lists for Ubuntu
RUN apt-get update

# install the Java OpenJDK 8 (not the Oracle Java JDK 8, which requires a license)
# the Java JDK includes the Java JRE as well
RUN apt install -y openjdk-8-jdk

# install git to be able to clone the heroku startup repository
RUN apt install -y git

# install maven to be able to build the heroku startup repository
RUN apt install -y maven


# === DATABASE SETUP - START ===
# install PostgreSQL to allow the web application to store data in the local database
# the DEBIAN_FRONTEND argument is needed to suppress the postgres prompts
# for manually selecting the region and time zone
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y postgresql

# change to 'postgres' user (created automatically when postgresql was installed using apt-get) to
# allow issuing commands to alter the database
USER postgres

# create a PostgreSQL role named 'postgresondockeruser' having the password 'postgresondockerpassword'
# and then create a database 'postgresondockerdb' owned by the 'postgresondockeruser' role.
#
# Note: The '&&\' is used to:
# - run multiple commands after one another (i.e., the '&&' part) and
# - allows the RUN command to span multiple lines (i.e., the '\' part)
RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER postgresondockeruser WITH SUPERUSER PASSWORD 'postgresondockerpassword';" &&\
    createdb --owner postgresondockeruser postgresondockerdb

# switch back to the root user for the remaining commands
USER root
# === DATABASE SETUP - END ===


# clone the heroku startup repository containing the Java Spring web application
RUN git clone https://github.com/heroku/java-getting-started

# all future commands will be relative to the cloned repository
WORKDIR /java-getting-started

# this steps illustrates that we can be very specific with the revision
# of the cloned repository we want to build, as it could be the case that
# the latest revision of the cloned repository may contain unstable changes or
# changes with unwanted side effects.
# Also, this practice promotes reproducibility of the image being built.
#
# Alternatively, we can specify a git tag instead of a sha, but for this example,
# since we do not have write access to the cloned repository, we use the sha of commit
# https://github.com/heroku/java-getting-started/commit/7894b41275e1bef0fa08c2b21f7835290f395099
RUN git reset --hard 7894b41275e1bef0fa08c2b21f7835290f395099


# === HACK FOR DEBUG AND GRADING - START ===
# insert this print statement at the beginning of the `dataSource()` method of the `Main` class
# to see the database url that was passed to the web server as an environment variable
#
# NOTE: this hack for debugging would only make sense if we specify the git commit sha in the
# `git reset --hard` command, where we know that at line 79 we could insert the print statement
# in the correct location:
# https://github.com/heroku/java-getting-started/blob/7894b41275e1bef0fa08c2b21f7835290f395099/src/main/java/com/example/Main.java#L79
#
# Obviously, in real world projects (and not in this simple example) in which we have write access
# to the cloned repository, we could setup proper logging and there will be no need for this hack
RUN sed -i '79i    System.out.println("dbUrl = " + dbUrl);' src/main/java/com/example/Main.java
# === HACK FOR DEBUG AND GRADING - END ===


# build the jar
RUN mvn install

# copy this script inside the WORKDIR of the image, to be run from inside the container
COPY start_inside_container.sh .
RUN chmod +x start_inside_container.sh

# configure the container to run as an executable
# (i.e., when a container is created and started, execute this script)
ENTRYPOINT ["./start_inside_container.sh"]
