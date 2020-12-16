#Build Image Storage

#JVM Image
#.\mvnw package
#docker build -f src/main/docker/Dockerfile.jvm.alpine -t malcolmpereira/imagestorage-quarkus-jvm .

#Native Image
#Make sure docker has alteast 6GB of memory allocated
.\mvnw package -Pnative '-Dquarkus.native.container-build=true' '-Dquarkus.native.container-runtime=docker' '-Dquarkus.container-image.build=true'
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagestorage-quarkus-native .
