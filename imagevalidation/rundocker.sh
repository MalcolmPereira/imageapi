#!/bin/zsh

#Runs a Docker Container for malcolmpereira/imagevalidation-quarkus-jvm

#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

#JVM image
docker run -dit --memory 128m --network imageapi-net --name imagevalidation -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8810:8810 malcolmpereira/imagevalidation-quarkus-jvm

