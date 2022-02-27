FROM openjdk:8-jdk-alpine
MAINTAINER Marwan
USER root
COPY target/ibm-wmla-0.0.1-SNAPSHOT.jar ibm-wmla-file-uploader.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
ENTRYPOINT ["java","-jar","/ibm-wmla-file-uploader.jar"]
