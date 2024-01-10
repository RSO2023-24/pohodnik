FROM eclipse-temurin:17-jre

RUN mkdir /app

WORKDIR /app

ADD ./api/target/pohodnik-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8082

CMD ["java", "-jar", "pohodnik-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "pohodnik-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar pohodnik-api-1.0.0-SNAPSHOT.jar
