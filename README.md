# Image API Microservices

## TL;DR

This article covers microservices implementation using Quarkus: Supersonic Subatomic Java. We look at Quarkus to realize a microservices architecture  for a simple image sampling service.

We will list steps to incorporate Quarkus extensions for OpenAPI service definition, REST clients, Eclipse micro profile metrics, Distributed Tracing and Centralized Logging. We also look at creating Docker images for JVM and building native images using GRAAL. The article concludes with deploying services to  Docker Compose and Kubernetes.

![Image API Microservices](ReadMe2.gif)


## Show me the code:
#### Code Description - https://github.com/MalcolmPereira/imageapi

## 1. Microservices Recap

#### What ?

Microservices offers a new paradigm for organizing applications when solving problems in a business domain. Microservices are an architectural style for separating concerns of a given domain into isolated independent services each providing concise business value. Services may work independently or invoke other services to meet business objectives, each service aims to provide one business function and do it well.

#### Why ?

Microservices provides opportunities for businesses to be resilient and agile in an ever competitive market and with incentives to break away from large distributed monoliths.
Microservices if implemented correctly will provide the following benefits:

- Highly coherent and loosely coupled services, each service is tasked with one business objective performing it well.

- Services can be owned, managed by smaller teams and can evolve independently while maintaining required business functions it supports. This helps with improved time to market, microservices compliments agile based development methodologies.

- Services can be independently developed, deployed, monitored and scaled.

#### How ?

Microservices are inherently developed as RESTful services built around RESTful verbs of GET, POST, PUT, DELETE. REST is a proven architectural style that complements microservices for exposing business functions apis as services. OpenAPI specification aids in documenting and describing the exposed services.

Internal inter-service communication may use other RPC technologies like gRPC which can speed up communications using binary data serialization instead of textual (JSON/XML) representations.
Many frameworks exists in almost all modern languages supporting REST constructs, this document aims to use Quarkus for realizing java based RESTful services.

#### … Challenges ?

Yes, like everything else in life there ain't no such thing as a free lunch.
Breaking apart monoliths into distributed services bring following challenges:

- Logging and Monitoring : There is no one place to look if things start to break, there are multiple services to monitor and even more components that can fail.

- Tracing : When services depend on other services, how can we trace requests from one service to another and determine end to end how a given request was processed.

- Reporting : Each service will have its own data store, data is now spread across multiple micro-databases, reporting becomes challenging.

- Service Discovery : How can services find each other or how callers of apis reach services

- Security : Security concerns need to be carefully examined for each service, more attack vectors to defend now that there are many services.

- Increased latency: When services need to work with other services to complete a request, latency will increase due to inter service communications, need to carefully examine inter service dependencies and ways to speed things up.

All of these and others need to be carefully designed for and planned when architecting for microservices.

In the end, many proven solutions exists today that solve these challenges giving microservices an edge that outweigh impediments. Quarkus provides extensions to address these challenges and businesses should not deter from investing in microservices.


## 2. Quarkus

#### What ?

Quarkus is a new super fantastic java framework for cloud native stacks, sponsored by RedHat. Quarkus allows to build performant java microservices that are cloud native ready. Quarkus primary built around containers first mindset making for an ideal contender in cloud native stacks. 

#### Why?

Quarkus is open sourced with strong community support and backing from proven industry leaders. Quarkus extension ecosystem provides commonly used cloud native extensions ready to support use cases in your applications. Quarkus brings developer joy by live reloading of code during development, improving productivity. 

#### How ?

Quarkus bootstrapping page on quarkus.io is the place to get started. Please see quarkus.io for all great features quarkus has to offer.

We will examine a simple microservice realization using Quarkus and extensions available within the quarkus ecosystem which  solve common challenges when developing microservices.

## 3. Microservice Realization

The basic idea involves separating image processing concerns into independent services.
The application allows user to upload and scale an image, list and view images previously scaled.

Not very cutting edge or next level of technology that will solve climate change, none the less good enough for realizing microservices prototyping with Quarkus.

### Image API microservices core components:

#### Image Client: 

This is a simple react application that will invoke ImageAPI Service.

#### ImageAPI Service: 

This is the proxy or api gateway for image processing services, this acts as the facade for all other services contained in the architecture. The advantage being, consumers need not know how to invoke individual services or keep track of where these individual services resides or service contracts involved. Consumers are only ever talking to this facade which will internally coordinate with other services.

#### ImageValidation Service: 

This service validates uploaded content to check if it is supported and returns error for invalid content.

#### ImageThumbnail Service: 

This service will scale image based on user selected parameters and return processed image back to user synchronously, service will also write data to a message broker which will be processed by the image storage service asynchronously to save processed image data in its datastore.

Images are upsampled or downsampled using BICUBIC, BILINEAR or NEAREST_NEIGHBOR algorithms supported in Java 2D. (As of this writing Graal Native Image does not support java 2D so native image cannot be created for modules using Java 2D - ImageValidation and ImageThumbnail device.)

#### RabbitMQ: 

Message broker that allows for asynchronous processing of image related data.

#### ImageStorageService: 

This service stores processed image data in PostgreSQL database and allows to view processed data at a later time.

#### PostgreSQL: 

Database to store processed images.

#### ELK Stack: 

ElasticSearch, LogStash and Kibana for Centralized Logging

#### Jaegar: 

Distributed Tracing support by Jaegar Open Tracing Implementation

### Basic flow described:

Consumer calls ImageAPI service facade to sample image, service facade coordinates with ImageValidation Service and ImageThumbnail Service to return sampled image synchronously, image data is written to message broker and processed asynchronously by ImageStorage Service to save image data to PostgreSQL datastore.

## 4. Implementation  with Quarkus

As described earlier, microservices pose significant challenges as we break away legacy monoliths to distributed services, Quarkus aids in microservices development by bridging these challenges with the following abilities making for a smoother transition to a resilient implementation.

#### 4.1 Simplicity:

Quarkus makes it very simple and straight forward to quickly get started building java microservices with the bootstrapping page. Configure application details, select extensions as needed and click generate application to download application zip.

Extract application zip file and open in your favorite IDE. The application has a basic scaffolding with all dependencies in place to start building the service.

The codebase is a maven project with regular maven configuration, additional docker folder containing pre-configured docker files and application.properties file which controls application configurations. JBOSS RESTEasy is the backbone supporting restful nature of the application.

Quarkus community has created well documented guides with step by step process for the various tooling and extensions supported in quarkus ecosystem, will refer to these guides as we explore how these are added to the image api service.
Run application using provided maven wrapper.

```./mvnw quarkus:dev```

The quarkus:dev plugin allows for hot code replacement with code updates quickly reloaded within a blink of an eye as you keep coding the application, true developer joy.

#### 4.2 Service Definition:

OpenAPI specification provides a standard way of describing microservices, defining api and contracts they support. The specification can be described in JSON or YAML, latter being more concise without clutter, omitting parenthesis.

Swagger is a well know implementation of OpenAPI specification and SwaggerUI generates UI to describe microservice endpoints, contracts and schema for request and responses and also provision  to quickly test the service.

The quarkus quarkus-smallrye-openapi extension provides required facility for describing your services and wrapping it in SwaggerUI which can be published on a known endpoint along with service.

The openapi-swaggerui guide helps to get started using open api in quarkus.
Steps for exposing open api service specification are as follows:

1. Add quarkus-smallrye-openapi maven dependency to the project.

2. Document your service following open api specification in JSON or YAML, YAML preferred under src/main/resources/META-INF/openapi.yaml.

``` 
openapi: 3.0.3

info:
  title: Image API
  description: Open API documentation for Image API
  version: 1.0.0

servers:
  - url: http://localhost:8080/imageapi
  - url: http://localhost:38080/imageapi

tags:
  - name: ImageAPI
    description: Image API Service API Endpoint
paths:
  /validate:
    post :
      description: Image Validation
      tags:
        - ImageAPI
      requestBody:
        description: Image Validation Data
        required: true
        content:
.......
```

3. Update quarkus application.properties file to include swagger-ui. and read from static openapi.yaml, by default quarkus will look for openapi.json, openapi.yaml/yml under resources.

I think having static file to document api contract is beneficial over dynamically generating it from codebase scanning packages and annotations, latter may feel magical, but one gives up control of well documented constructs over endless annotations.

4. Access using swagger ui to examine service using the /api endpoint.


#### 4.3 Rest Clients:

The Image API microservice implementation uses a facade implemented via the ImageAPI Service, this api gateway shields internal services form being exposed publicly, all consumers of the 

Image API microservice only need to know about api gateway, this gateway service coordinates with other services.

Quarkus makes it very simple to code rest clients using annotations, shielding away boiler plate code that we may generally be accustomed to.

The rest client guide helps to get started for woking with rest clients.

1. Add quarkus-rest-client maven dependency to the project.

2. Define rest client schema modeling data exchange as per contract for the specified service that needs to be invoke. Project lombok java library uses annotation to decorate java classes saving lot of typing writing those pesky getters/setters /constructors which are needed for java models.

```
com.malcolm.imageapi.domain.ImageUpload.java
....
@RegisterForReflection
public class ImageUpload {
    @FormParam("image")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File image;

    @FormParam("scale")
    @PartType(MediaType.TEXT_PLAIN)
    public String scale;

    @FormParam("scaletype")
    @PartType(MediaType.TEXT_PLAIN)
    @DefaultValue(value = "BICUBIC")
    public String scaletype;

    @FormParam("width")
    @PartType(MediaType.TEXT_PLAIN)
    public String width;

    @FormParam("height")
    @PartType(MediaType.TEXT_PLAIN)
    public String height;
}
```
3. Define rest client Interface and inject rest client interface to rest resource implementation.

```
com.malcolm.imageapi.ImageStorageService.java
....
@RegisterRestClient
public interface ImageStorageService {

@GET
@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
List<Image> listImages();

@GET
@Path("/images/{imageid}")
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,"image/png"})Response getImage(@PathParam("imageid") String imageid);
}
```

```
com.malcolm.imageapi.ImageAPIResource.java
.....
@Inject
@RestClient
ImageStorageService storageService;
public List<Image> listImages() throws ImageCheckException {
 ...
 return storageService.listImages();
 .....
}
```

4. Define rest client endpoint in application.properties file.

The rest client service interface with mp-rest url defines endpoint used when invoking service, environment variables can be set using prescribed ${ENV_VARIABLE:<default>} format.

Note:
Any non HTTP 200 status code from service endpoints should be handled explicility catching javax.ws.rs.WebApplicationException with an exception mapper to handle exceptions from services invoked by the rest clients.

```
com.malcolm.imageapi.exception.ImageCheckRestClientExceptionMapper.java
.....

@Override
public Response toResponse(WebApplicationException err) {
StatusResponse statusResponse;
try {
statusResponse = objectMapper.readValue(new String(IOUtils.toByteArray((InputStream) err.getResponse().getEntity())), StatusResponse.class);
} catch (IOException ioErr) {
statusResponse = new StatusResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), ImageCheckResourceError.SERVICE_ERROR.toString(), ImageCheckResourceError.SERVICE_ERROR.getErrorMessage());
}
return Response.status(statusResponse.getStatusCode()).entity(statusResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
```

## 5. Distributed Tracing

Distributed tracing provides a way to trace requests end to end as requests are processed by microservices. Jeagar is an implementation for open tracing specification providing observability across services that make up microservices architecture. Distributed tracing helps to monitor distributed transactions, performance, latency metrics and service dependencies.

Quarkus quarkus-smallrye-opentracing extension adds distributed tracing in a non intrusive manner using annotations, there are no code changes required in most cases. When services invoke other services using rest clients distributed context is automatically propagated to the service being invoked as long as calling service also supports distributed tracing and can assemble the distributed context during its execution.

The open tracing guide helps to get started for distributed tracing.

1. Add quarkus-smallrye-opentracing maven dependency, That is all, there is no tracing specific code required in the application itself for most cases

2. Update application.properties with jaegar service specific details i.e the jaegar service host and port.

I am running Jaegar all in one tracing docker image -jaegertracing/all-in-one:1.18, this is the easiest to get started with distributed tracing.

3. In most cases distributed tracing works without any code updates, however in certain scenarios where asynchronous processing is involved we may need some way to pass distributed context to downstream services.

In ImageThumbnail service, distributed tracing context is put on the RabbitMQ message broker along with image data, ImageStorage service reassembles distributed tracing context reading from message queue so that request can be traced across synchronous and asynchronous patterns of request processing.

Open Tracing  inject is used to extract distributed context and save to a text map, this can then be put to a message queue as key value pairs.

```
com.malcolm.imagesampling.ImageThumbnailResource.java
...
final Map<String,String> textMap = new HashMap<>();
tracer.inject(tracer.activeSpan().context(), Format.Builtin.TEXT_MAP, new TextMap() {
    @Override
    public Iterator<Map.Entry<String,String>> iterator() {
        throw new UnsupportedOperationException("iterator not supported with Tracer.inject()");
    }
    @Override
    public void put(final String key, final String value) {
        textMap.put(key, value);
    }
});
...
```

Open API extract is then used to read from message queue and assemble distributed context in the ImageStorage service.

```
com.malcolm.imagestorage.ImageStorage.java
...
...
@SuppressWarnings("rawtypes")
Map messagedata = message.getBody(Map.class);
if(messagedata != null){
   final Map<String,String> textMap = new HashMap<>();
   messagedata.forEach((key, value) -> {
   ...
   }else{
     textMap.put(key.toString(),(String)value);
   .... 
   });
   final Span span = tracer.buildSpan("ImageStorage").addReference(References.FOLLOWS_FROM,tracer.extract(Format.Builtin.TEXT_MAP,new TextMapExtractAdapter(textMap))).start();
    tracer.scopeManager().activate(span,true);
...
...
```

4. On execution of the services distributed traces are logged for specific service and also in the Jaegar services. Distributed traceId and spanId can be validated in the services logs and on jaegar ui.

Access Jaegar UI via http://localhost:16686/


## 6. Centralized Logging

Logging becomes particular challenging as we break away from monolithic applications to distributed services based architecture. Having access to logs for all services at a centralized location and easy way of making sense of those logs becomes vital when troubleshooting microservices.

The ELK stack (ElasticSearch, Logstash and Kibana) and similar stacks support collecting logs for microservices to central location.

Quarkus GELF logging extension applies GELF log handler to the underlying log manager and post logs to a centralized logging system using.

Combined with Distributed Tracing using Trace Id and Span ID, ELK stack provides a centralized location for validating logs.

The centralized log management guide helps to get started for distributed logging.

Add quarkus-logging-gelf maven dependency, this creates a GELF log handler over underlying JBOSS log handler.

1. Update application.properties with GELF handler configuration for centralized logging host. This is a ELK stack with a GELF plugin for logstash. It is very important to set full mdc context to true, this allows to propagate the traceId and spanId into logstash.

2. Validate logs from the Kibana UI and filter by Trace ID and Span ID.

I am running ELK stack via docker images from Docker Hub: Elastisearch, Logstash and Kibana.

Note:
GELF plugin needs to be enabled for LogStash, gelf plugin can be enabled via environment variables for log stash docker image.
```
docker-compose.yml
...
logstash:
    image: logstash:7.9.2
    deploy:
      resources:
        limits:
          cpus: 0.50
          memory: "512M"
    environment:
      - config.string= '
        input {
        gelf {
        port => 12201
        }
        }
        output {
        stdout {}
        elasticsearch {
        hosts => ["http://elasticsearch:9200"]
        }
        }'
    ports:
      - "12201:12201/udp"
      - "5000:5000"
      - "9600:9600"
...
```


## 7. Metrics

Metrics provides information about api calls and operations characteristics, this service telemetry related data can be exposed and collected by management agents to create dashboards about collected metrics.

The eclipse microprofile-metrics project provides a unified way for exporting monitoring data. 

The microprofile-metrics guide provides overview about integrating metrics to the application.

1. Add quarkus-smallrye-metrics maven dependency.

2. Add annotations for the metrics to be exposed, available metrics are @Counted, @ConcurrentGauge, @Gauge, @Metered, @Metric, @Timed, @SimplyTimed which can be applicable to constructors, methods or fields.

```
com.malcolm.imageapi.ImageAPIResource.java
....
@Timed(name = "ImageAPI-Validate-Timer", displayName = "ImageAPI-Validate-Timer", description = "Measure ImageAPI Validation Performance.", unit = MetricUnits.SECONDS)
@Counted(name = "ImageAPI-Validate-Counter", displayName = "ImageAPI-Validate-Counter", description = "Number of ImageAPI Validations Performed.")
@Metered(name = "ImageAPI-Validate-Meter",displayName = "ImageAPI-Validate-Meter", description = "Number of ImageAPI Validations Metered.")
public ImageMetadata validate(@MultipartForm ImageUpload imageUpload) throws ImageCheckException {
    try {
...
...
```

3. Generated metrics data can be accumulated by calling metrics endpoint -http://localhost:8080/metrics/application. This can be consumed by monitoring applications to create custom dashboards around the performance of the service.

## 8. Code
Repository: https://github.com/MalcolmPereira/imageapi

_Pre-requisites: _
Java 11 or later, Docker Desktop, Kubernetes (Docker Desktop already comes with Kubernetes) , Understanding of Maven, Docker, Kubernetes and machine having 8GB of memory to spare. Docker running ELK stack…hmmm…takes up lot of resources.

The Image API microservice code consists of the following projects:

#### docker-compose : 
This contains docker-compose yaml file for deploying services to docker compose.

#### imageapi: 
This contains Quarkus ImageAPI microservice. ImageAPI is proxy or api gateway for 
image processing services and acts as facade for all other services contained in the architecture. 

#### imageclient: 
This contains a simple react ui application that will invoke ImageAPI microservice.

#### imagestorage: 
This contains Quarkus ImageStorage microservice. ImageStorage service stores processed image data in PostgreSQL database and allows to view processed data at a later time.

#### imagethumbnail: 
This contains Quarkus ImageThumbnail microservice. ImageThumbnail service will sample images based on user selected parameters synchronously, service will also write data to a message broker which will be processed by image storage service asynchronously to save image data in its datastore.

#### imagevalidation: 
This contains Quarkus ImageValidation microservice. ImageValidation service validates uploaded content to check if it is supported and returns error for invalid content.

#### jaegar-tracing: 
This contains run-jaegar-docker.sh to run standalone jaegar service, basically downloads jaegertracing/all-in-one:1.18 docker image and runs it.

#### kubernetes: 
This contains all kubernetes yaml file for deploying image api microservice and related supporting services to local kubernetes node.

#### postgresql-docker: 
This contains Dockerfile definition to build postgresql database containing imageapi database and image table to store images.

#### rabbitmq-docker: 
This contains Dockerfile definition to build RabbitMQ image which enables AMQP 1.0 protocol.

#### imageapi-docker-build.sh: 
zsh script to build all docker images for ImageAPI microservices and related services.

#### imageapi-docker-run.sh: 
zsh script to run stand alone docker container for ImageAPI microservices and related services. Please see note about imageclient/.env file in Deploying to Docker Compose before running stand alone docker containers.

The Elastic Logstash Kibana stacks are defined in docker-compose and the kubernetes yaml files to create the ELK stack services, GELF handler is configured in the quarkus microservices using the quarkus gelf logging handler extensions.

_Image Processing: _
Core logic for image processing is in ImageThumbnailService

```
com.malcolm.imagesampling.ImageThumbnailService.java
...
private BufferedImage scaleImage(BufferedImage img, double scalex, double scaley, ImageScaleSampling scaleType) {
...
final AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
AffineTransformOp ato;
if(ImageScaleSampling.BICUBIC  == scaleType){
ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
}else if(ImageScaleSampling.BILINEAR  == scaleType){
ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
}else if(ImageScaleSampling.NEAREST_NEIGHBOR  == scaleType){
ato = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
}else{
ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
}
return ato.filter(img, newImg);
...
```

_Environment Variables:_

#### imageclient : 

The .env contains url of ImageAPI microservice gateway. This points to the Kubernetes Nodeport 31001 by default. This is used when building react ui application. Any change to URL requires rebuilding of react ui application and generating a new docker image.
```
REACT_APP_API_URL=http://localhost:31001/imageapi/
```

#### imageapi: 

The Quarkus application.properties files in java microservice contains environment variable for the following. 

CORS URI  for react ui application 

Logstash host URI for  Centralized Logging

Jaegar host URI for Distributed Tracing

ImageValidation Service Endpoint, ImageThumbnail Service Endpoint and ImageStorage Service Endpoint

```
...
quarkus.http.cors.origins=${IMAGE_API_ORIGINS:http://localhost:3000}
...
quarkus.log.handler.gelf.host=${LOGSTASH_HOST:localhost}
...
quarkus.jaeger.endpoint=${JAEGAR_HOST:http://localhost:14268/api/traces}
com.malcolm.imageapi.ImageValidationService/mp-rest/url=${IMAGEVALIDATION_SERVICE:http://localhost:8810/imagevalidation}
com.malcolm.imageapi.ImageThumbnailService/mp-rest/url=${IMAGETHUMBNAIL_SERVICE:http://localhost:8820/imagesampling}
com.malcolm.imageapi.ImageStorageService/mp-rest/url=${IMAGESTORAGE_SERVICE:http://localhost:8830/imagestorage}
...
```

#### imagevalidation:

The Quarkus application.properties contains environment variable for the following.

Logstash host URI for Centralized Logging

Jaegar host URI for Distributed Tracing

```
...
quarkus.log.handler.gelf.host=${LOGSTASH_HOST:localhost}
...
quarkus.jaeger.endpoint=${JAEGAR_HOST:http://localhost:14268/api/traces}
```

#### imagethumbnail: 

The Quarkus application.properties contains environment variable for the following.

Logstash host URI for Centralized Logging

Jaegar host URI for Distributed Tracing

RabbitMQ message broker for asynchronous processesing of image data

```
...
quarkus.log.handler.gelf.host=${LOGSTASH_HOST:localhost}
...
quarkus.jaeger.endpoint=${JAEGAR_HOST:http://localhost:14268/api/traces}
...
quarkus.qpid-jms.url=amqp://${IMAGE_RABBITMQ_HOST:localhost}:5672
quarkus.qpid-jms.username=${IMAGE_RABBITMQ_USERNAME:imageapi}
quarkus.qpid-jms.password=${IMAGE_RABBITMQ_PASSWORD:secret}
```

#### imagestorage: 

The Quarkus application.properties contains environment variable for the following.

Logstash host URI for Centralized Logging

Jaegar host URI for Distributed Tracing

RabbitMQ message broker for asynchronous processesing of image data

PostGreSQL datastore for image data

```
...
quarkus.log.handler.gelf.host=${LOGSTASH_HOST:localhost}
...
quarkus.jaeger.endpoint=${JAEGAR_HOST:http://localhost:14268/api/traces}
...
quarkus.qpid-jms.url=amqp://${IMAGE_RABBITMQ_HOST:localhost}:5672
quarkus.qpid-jms.username=${IMAGE_RABBITMQ_USERNAME:imageapi}
quarkus.qpid-jms.password=${IMAGE_RABBITMQ_PASSWORD:secret}
...
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=secret
quarkus.datasource.jdbc.url=jdbc:postgresql://${IMAGEAPI_DB_HOST:localhost}:5432/imageapi
....
```
All environment variables are passed from docker compose and kubernetes deployment yamls.

## 9. Docker Images

Generated Quarkus projects are  pre-configured with Dockerfile for JVM (Dockerfile.jvm) and Dockerfile for Native image (Dockerfile.native). To minimize the size of the JVM image I added Dockerfile.jvm.alpine which builds from jdk-11.0.8_10-alpine-slim to keep the JVM image smaller.

Docker images are built running imageapi-docker-build.sh script. This script will call individual mvn builds and docker builds on various projects to generate docker images for the Image API microservices, RabbitMQ and PostGreSQL. 

Jaegar Tracing and ELK (ElasticSearch / LogStash / Kibana) stack containers are run from their default images available form Docker Hub so no docker build required for them.

#### _Native Images _

Quarkus allows to build native images using GRAALVM. The native build is executed on Docker by downloading quay.io/quarkus/ubi-quarkus-native-image for GRAAL compile and native image generation. GRAALVM local installation is not required. 
Generating native images is a compute and memory intensive process. The native image build takes a while to complete anywhere from 10–20 minutes. Please configure at least 7GB of memory for Docker via Docker resource preferences when generating native image.

Generating native images is slow and error prone, sometimes build thread just goes into deadlock and process needs to be killed and restarted, inspite of these drawbacks the benefits are worth the effort. Native images take up very little memory and start up very quickly compared to a JVM image.

Native images are generated for ImageAPI and ImageStorage.

```
...
mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imageapi-quarkus-native .
mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.container-image.build=true
docker build -f src/main/docker/Dockerfile.native -t malcolmpereira/imagestorage-quarkus-native .
...
```

ImageValidation and ImageThumbnail services make use of java.awt.image.BufferedImage for image validation and image sampling, unfortunately java.awt not supported on GRAAL VM as of this writing. Native images fail to generate for these to services.

## 10. Docker Compose

Please update the imageclient/.env file before running docker compose and rebuild imageclient docker image.

The ImageClient api invokes the  ImageAPI gateway service. The default  value points to Kubernetes Nodeport 31001 - http://localhost:31001/imageapi. We need to update this to http://localhost:8080/imageapi when running docker container or docker-compose.

```
imageclient/.env file
...
REACT_APP_API_URL=http://localhost:31001/imageapi/
```

Rebuild imageclient docker image using imageclient/buildDocker.sh script.

Resource limits are set on services so that all services can start up and be functional. ELK stack takes up most resources almost 2GB of memory and slowest to start up.

```
docker-compose.yml
...
image: elasticsearch:7.9.2
deploy:
resources:
limits:
cpus: 0.50
memory: "1.5G"
```

ImageAPI and ImageStorage native images take least amount of resources and these are fastest to start up.

```
docker-compose.yml
...
imageapi:
image: malcolmpereira/imageapi-quarkus-native
deploy:
resources:
limits:
cpus: 0.25
memory: "8M"
```
Use docker-compose up on docker-compose.yml to deploy Image API Services.

Please tweak docker-compose files in terms of cpu/memory resource limits to get all containers running locally. ELK containers can be omitted if machine is resource constrained at the cost of losing out centralized logging.

The react UI application is accessible via port 3000.

The ImageAPI Gateway Service Open API Service Definition is accessible via port 8080.

The ImageAPI service metrics is accessible via port 8080.

The Distributed Tracing Jaegar UI is accessible via port 16686.

Centralized Logging Kibana dashboard is accessible via port 5601.

## 11. Kubernetes

The Kubernetes Extension guide helps to get started generating Kubernetes resources.
Add quarkus-kubernetes maven dependency to the project.

Running ./mvnw package generates Kubernetes yaml files under target/kubernetes/ directory. I have moved all generated Kubernetes yaml files for Quarkus projects to kubernetes directory and also created resources for supporting services.

Please update imageclient/.env file if this was previously updated to run on docker compose or standalone docker container.
The ImageClient api URL needs to reach Kubernetes Nodeport 31001 http://localhost:31001/imageapi.

```
imageclient/.env file
...
REACT_APP_API_URL=http://localhost:31001/imageapi/
```

Rebuild imageclient docker image using imageclient/buildDocker.sh script.

Similar to docker compose please tweak kubernetes yaml file for cpu and memory resource constraints. 

ELK stack consume most resources.

```
elasticsearch-kubernetes.yml
...
serviceAccount: elasticsearch
containers:
- name: elasticsearch
image: elasticsearch:7.9.2
imagePullPolicy: Never
resources:
limits:
memory: "2.5Gi"
requests:
memory: "2.5Gi"
...
```

Native images consume least resources.

```
imageapi-kubernetes.yml
...
containers:
- name: imageapi
image: malcolmpereira/imageapi-quarkus-native
imagePullPolicy: Never
resources:
requests:
cpu: "0.25"
memory: "15Mi"
limits:
cpu: "0.25"
memory: "15Mi"
...
```

Run kubernetes/apply.sh script to deploy kubernetes resources.

The react ui application is accessible via Kubernetes Nodeport 31000.

The ImageAPI service Open API Definition is accessible via Kubernetes Nodeport 31001

ImageAPI service metrics accessible via Kubernetes Nodeport 31001.

Distributed Tracing Jaegar ui is accessible via Kubernetes Nodeport 31002.

Centralized Logging Kibana dashboard is accessible via Kubernetes Nodeport 31003.


