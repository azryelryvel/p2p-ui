FROM mariadb:10.3

ENV MYSQL_ROOT_PASSWORD PASSWORD
ENV MYSQL_PASSWORD PASSWORD
ENV MYSQL_USERNAME p2p
ENV MYSQL_DATABASE p2p

RUN echo "grant all on p2p.* to 'p2p'@'172.17.%' identified by 'PASSWORD';" >  /docker-entrypoint-initdb.d/docker-local-grants.sql