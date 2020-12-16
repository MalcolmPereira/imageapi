#Runs a Docker Container for malcolmpereira/imagevalidation-quarkus-jvm

#Create User Defined Network
docker network create --driver bridge imageapi-net

#JVM image
docker run -dit --memory 128m --network imageapi-net --name imagevalidation -e JAEGAR_HOST=http://jaeger:14268/api/traces -e LOGSTASH_HOST=logstash -p 8810:8810 malcolmpereira/imagevalidation-quarkus-jvm

