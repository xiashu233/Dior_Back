FROM java:8
VOLUME /tmp
COPY target/resource-1.0-SNAPSHOT.jar resource.jar
RUN bash -c "touch /resource.jar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","resource.jar"]
