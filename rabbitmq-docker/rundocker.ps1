
#Runs a Docker Container for Rabbit MQ
#Access Rabbit MQ via http://localhost:15672 imageapi secret

#Create User Defined Network
docker network create --driver bridge imageapi-net

docker run -dit --network imageapi-net --name rabbitmq --memory 256m -e RABBITMQ_DEFAULT_USER=imageapi -e RABBITMQ_DEFAULT_PASS=secret -p 15672:15672 -p 5672:5672  malcolmpereira/imageapi-rabbitmq

