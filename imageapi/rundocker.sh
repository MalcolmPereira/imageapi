#!/bin/zsh

#Runs a Docker Container for malcolmpereira/imageapi-quarkus-native

#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

#Native image
docker run -dit --memory 15m --network imageapi-net --name imageapi -e IMAGEVALIDATION_SERVICE=http://imagevalidation:8810/imagevalidation -e IMAGETHUMBNAIL_SERVICE=http://imagethumbnail:8820/imagesampling -e IMAGESTORAGE_SERVICE=http://imagestorage:8830/imagestorage -e IMAGE_API_ORIGINS=http://localhost:3000 -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8080:8080 malcolmpereira/imageapi-quarkus-native
