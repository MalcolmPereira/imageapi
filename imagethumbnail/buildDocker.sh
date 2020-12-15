#! /bin/zsh


#Build Image Thumbnail

#JVM Image
mvn package
docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagethumbnail-quarkus-jvm .
#Native Image Not Possible At this time since GRAAL does not support java.awt Graphics/BufferedImage
