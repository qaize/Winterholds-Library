FROM openjdk:17

VOLUME /temp

EXPOSE 8080

ADD target/AppWinterhold-0.0.1-SNAPSHOT.jar App.jar

ENTRYPOINT ["java","-jar","App.jar"]

#docker run -d -p 8080:8080 winterholds