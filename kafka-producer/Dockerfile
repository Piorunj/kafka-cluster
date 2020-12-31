FROM openjdk:11-jre-slim
VOLUME /tmp
EXPOSE 8081
ADD target/kafka-producer-0.0.1-SNAPSHOT.jar kafka-producer.jar
ENTRYPOINT ["java","-jar","kafka-producer.jar"]