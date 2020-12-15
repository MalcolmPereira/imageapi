FROM postgres:13.1
# Run the rest of the commands as the ``postgres`` 
USER postgres

#COPY the db script
COPY imageapi.sql /docker-entrypoint-initdb.d/
