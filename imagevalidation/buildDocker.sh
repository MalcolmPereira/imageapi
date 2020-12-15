#! /bin/zsh


#Build Image Validation

#JVM Image
mvnw package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagevalidation-quarkus-jvm .

#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
#mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true -Dquarkus.native.native-image-xmx=3g
#docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagevalidation-quarkus-native .
