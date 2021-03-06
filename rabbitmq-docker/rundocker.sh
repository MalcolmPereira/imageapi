#!/bin/zsh

#Runs a Docker Container for Rabbit MQ
#Please see https://www.rabbitmq.com/getstarted.html
#https://hub.docker.com/_/rabbitmq for rabbit mq with AMQP 1.0 emabled
#Access Rabbit MQ via http://localhost:15672 imageapi secret

#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

docker run -dit --network imageapi-net --name rabbitmq --memory 256m -e RABBITMQ_DEFAULT_USER=imageapi -e RABBITMQ_DEFAULT_PASS=secret -p 15672:15672 -p 5672:5672  malcolmpereira/imageapi-rabbitmq

