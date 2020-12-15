#!/bin/zsh

#Runs a Docker Container for PostGres
#Please see https://hub.docker.com/_/postgres

#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

docker run -dit --memory 128m --network imageapi-net --name postgresql -e POSTGRES_PASSWORD=secret -p 5432:5432 malcolmpereira/imageapi-postgres