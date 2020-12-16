#Run Stanalone imagethumbnail service

#Create User Defined Network
docker network create --driver bridge imageapi-net

#JVM image
docker run -dit --memory 128m --network imageapi-net --name imagethumbnail -e IMAGE_RABBITMQ_HOST=rabbitmq -e IMAGE_RABBITMQ_USERNAME=imageapi -e IMAGE_RABBITMQ_PASSWORD=secret -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8820:8820 malcolmpereira/imagethumbnail-quarkus-jvm

