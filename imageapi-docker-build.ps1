echo "Starting Docker Image Builds..."

#Build Image API
cd imageapi
#JVM Image
#mvnw package
#docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imageapi-quarkus-jvm .
#Native Image
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imageapi-quarkus-native .
cd ..


#Build Image Validation
cd imagevalidation
#JVM Image
mvnw package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagevalidation-quarkus-jvm .
#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
#docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagevalidation-quarkus-native .
cd ..

#Build Image Thumbnail
cd imagethumbnail
#JVM Image
mvnw package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagethumbnail-quarkus-jvm .
#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
#docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagethumbnail-quarkus-native .
cd ..

#Build Image Storage
cd imagestorage
#JVM Image
#mvnw package
#docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagestorage-quarkus-jvm .
#Native Image
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagestorage-quarkus-native .
cd ..

#Build Image Client
cd imageclient
yarn install
yarn build
docker build -t malcolmpereira/imageclient .
cd ..

#Build PostGreSQL Image
cd postgresql-docker
docker build -t malcolmpereira/imageapi-postgres .
cd ..

#Build RabbitMQ with AMQP 1.0 Image
cd rabbitmq-docker
docker build -t malcolmpereira/imageapi-rabbitmq .
cd ..

echo "Done Docker Image Builds: "
echo "Image API malcolmpereira/imageapi-quarkus-jvm "
echo "Image Validation Service malcolmpereira/imagevalidation-quarkus-jvm "
echo "Image Thumbnail Service malcolmpereira/imagethumbnail-quarkus-jvm "
echo "Image Storage Service imagestorage-quarkus-jvm "
echo "Image React Client malcolmpereira/imageclient "
echo "Image DataStore malcolmpereira/imageapi-postgres"
echo "Image Queue malcolmpereira/imageapi-rabbitmq"