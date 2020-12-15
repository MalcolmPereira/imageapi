echo "Starting Standalone Docker Run"

#Run RabbitMQ with AMQP 1.0 Image
cd rabbitmq-docker
./rundocker.ps1
cd ..

#Run PostGreSQL Image
cd postgresql-docker
./rundocker.ps1
cd ..

#Run Jaegar Tracing
cd jaegar-tracing
./run-jaegar-docker.ps1
cd ..

#Run Image Storage
cd imagestorage
./rundocker.ps1
cd ..

#Run Image Thumbnail
cd imagethumbnail
./rundocker.ps1
cd ..

#Run Image Validation
cd imagevalidation
./rundocker.ps1
cd ..

#Run Image API
cd imageapi
./rundocker.ps1
cd ..

#Run Image Client
cd imageclient
./rundocker.ps1
cd ..

echo "Done Docker Standalone Run "