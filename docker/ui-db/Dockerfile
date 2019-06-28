FROM mariadb:10.3

ENV MYSQL_ROOT_PASSWORD PASSWORD
ENV MYSQL_PASSWORD PASSWORD
ENV MYSQL_USERNAME p2pd
ENV MYSQL_DATABASE p2pd

RUN echo "grant all on p2pd.* to 'p2pd'@'172.17.%' identified by 'PASSWORD';" >  /docker-entrypoint-initdb.d/docker-local-grants.sql