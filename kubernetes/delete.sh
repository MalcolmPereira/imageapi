#!/bin/zsh

kubectl delete -f jaegar-kubernetes.yml
kubectl delete -f rabbitmq-kubernetes.yml
kubectl delete -f postgres-kubernetes.yml
kubectl delete -f imagestorage-kubernetes.yml
kubectl delete -f imagethumbnail-kubernetes.yml
kubectl delete -f imagevalidation-kubernetes.yml
kubectl delete -f imageapi-kubernetes.yml
kubectl delete -f imageclient-kubernetes.yml
kubectl delete -f elasticsearch-kubernetes.yml
kubectl delete -f logstash-kubernetes.yml
kubectl delete -f kibana-kubernetes.yml