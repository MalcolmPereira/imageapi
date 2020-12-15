#! /bin/zsh

#Check and Create imageapi bridge network if not available when running standalone docker
networkCount=`docker network ls | grep imageapi-net | wc -l`
networkCount=$(($networkCount + 0))
if [[ $networkCount = "0" ]]
then
    docker network create --driver bridge imageapi-net
fi

docker run -dit --memory 8m --network imageapi-net --name imageclient -p 3000:80 malcolmpereira/imageclient