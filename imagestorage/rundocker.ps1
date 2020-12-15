#Runs a Docker Container for malcolmpereira/imagestorage-quarkus-jvm


docker network create --driver bridge imageapi-net

#Native image
docker run -dit --memory 15m --network imageapi-net --name imagestorage -e IMAGE_RABBITMQ_HOST=rabbitmq -e IMAGE_RABBITMQ_USERNAME=imageapi -e IMAGE_RABBITMQ_PASSWORD=secret -e IMAGEAPI_DB_HOST=postgresql -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8830:8830 malcolmpereira/imagestorage-quarkus-native

