docker network create --driver bridge imageapi-net

docker run -dit --memory 8m --network imageapi-net --name imageclient -p 3000:80 malcolmpereira/imageclient