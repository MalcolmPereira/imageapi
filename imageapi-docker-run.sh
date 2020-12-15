#! /bin/zsh

echo "Starting Standalone Docker Run"

#Run RabbitMQ with AMQP 1.0 Image
cd rabbitmq-docker
./rundocker.sh
cd ..

#Run PostGreSQL Image
cd postgresql-docker
./rundocker.sh
cd ..

#Run Jaegar Tracing
cd jaegar-tracing
./run-jaegar-docker.sh
cd ..

#Run Image Storage
cd imagestorage
./rundocker.sh
cd ..

#Run Image Thumbnail
cd imagethumbnail
./rundocker.sh
cd ..

#Run Image Validation
cd imagevalidation
./rundocker.sh
cd ..

#Run Image API
cd imageapi
./rundocker.sh
cd ..

#Run Image Client
cd imageclient
./rundocker.sh
cd ..

echo "Done Docker Standalone Run "