#MYSQL 8.0
FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD="Db@1234"
ENV MYSQL_DATABASE="blogpost"

ADD db_backup.sql docker-entrypoint-initdb.d/db_backup.sql