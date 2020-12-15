#!/bin/zsh

#Runs a Docker Container for malcolmpereira/imagestorage-quarkus-jvm


#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

#Native image
docker run -dit --memory 15m --network imageapi-net --name imagestorage -e IMAGE_RABBITMQ_HOST=rabbitmq -e IMAGE_RABBITMQ_USERNAME=imageapi -e IMAGE_RABBITMQ_PASSWORD=secret -e IMAGEAPI_DB_HOST=postgresql -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8830:8830 malcolmpereira/imagestorage-quarkus-native

