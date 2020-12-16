#Runs a Docker Container for malcolmpereira/imageapi-quarkus-native
#Create User Defined Network
docker network create --driver bridge imageapi-net

#Native image
docker run -dit --memory 15m --network imageapi-net --name imageapi -e IMAGEVALIDATION_SERVICE=http://imagevalidation:8810/imagevalidation -e IMAGETHUMBNAIL_SERVICE=http://imagethumbnail:8820/imagesampling -e IMAGESTORAGE_SERVICE=http://imagestorage:8830/imagestorage -e IMAGE_API_ORIGINS=http://localhost:3000 -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8080:8080 malcolmpereira/imageapi-quarkus-native
