#!/bin/zsh

kubectl apply -f jaegar-kubernetes.yml
kubectl apply -f rabbitmq-kubernetes.yml
kubectl apply -f postgres-kubernetes.yml
kubectl apply -f imagestorage-kubernetes.yml
kubectl apply -f imagethumbnail-kubernetes.yml
kubectl apply -f imagevalidation-kubernetes.yml
kubectl apply -f imageapi-kubernetes.yml
kubectl apply -f imageclient-kubernetes.yml
kubectl apply -f elasticsearch-kubernetes.yml
kubectl apply -f logstash-kubernetes.yml
kubectl apply -f kibana-kubernetes.yml



#Node Ports:
#ImageClient         : 31000
#ImageAPI            : 31001
#Jaegar              : 31002
#Kibana              : 31003

#ImageValidation     : None
#ImageThumbnail      : None
#ImageStorage        : None
#RabbitMQ            : None
#PostGres            : None
#ElasticSearch       : None
#Logstash            : None
