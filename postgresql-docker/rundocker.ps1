docker network create --driver bridge imageapi-net


docker run -dit --memory 128m --network imageapi-net --name postgresql -e POSTGRES_PASSWORD=secret -p 5432:5432 malcolmpereira/imageapi-postgres