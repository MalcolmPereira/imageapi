Write-Output "Starting Docker Image Builds..."

#Build Image API
Set-Location imageapi
#JVM Image
#.\mvnw package
#docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imageapi-quarkus-jvm .
#Native Image
.\mvnw package -Pnative '-Dquarkus.native.container-build=true' '-Dquarkus.native.container-runtime=docker' '-Dquarkus.container-image.build=true'
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imageapi-quarkus-native .
Set-Location ..


#Build Image Validation
Set-Location imagevalidation
#JVM Image
.\mvnw package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagevalidation-quarkus-jvm .
#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
#.\mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
#docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagevalidation-quarkus-native .
Set-Location ..

#Build Image Thumbnail
Set-Location imagethumbnail
#JVM Image
.\mvnw package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagethumbnail-quarkus-jvm .
#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
#.\mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
#docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagethumbnail-quarkus-native .
Set-Location ..

#Build Image Storage
Set-Location imagestorage
#JVM Image
#.\mvnw package
#docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagestorage-quarkus-jvm .
#Native Image
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
.\mvnw package -Pnative '-Dquarkus.native.container-build=true' '-Dquarkus.native.container-runtime=docker' '-Dquarkus.container-image.build=true'
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagestorage-quarkus-native .
Set-Location ..

#Build Image Client
Set-Location imageclient
yarn install
yarn build
docker build -t malcolmpereira/imageclient .
Set-Location ..

#Build PostGreSQL Image
Set-Location postgresql-docker
docker build -t malcolmpereira/imageapi-postgres .
Set-Location ..

#Build RabbitMQ with AMQP 1.0 Image
Set-Location rabbitmq-docker
docker build -t malcolmpereira/imageapi-rabbitmq .
Set-Location ..

Write-Output "Done Docker Image Builds: "
Write-Output "Image API malcolmpereira/imageapi-quarkus-jvm "
Write-Output "Image Validation Service malcolmpereira/imagevalidation-quarkus-jvm "
Write-Output "Image Thumbnail Service malcolmpereira/imagethumbnail-quarkus-jvm "
Write-Output "Image Storage Service imagestorage-quarkus-jvm "
Write-Output "Image React Client malcolmpereira/imageclient "
Write-Output "Image DataStore malcolmpereira/imageapi-postgres"
Write-Output "Image Queue malcolmpereira/imageapi-rabbitmq"