FROM openjdk:11.0.7-jre-slim
COPY target/*.jar app.jar

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait

CMD ./wait && java -jar app.jar -Xms100m -Xmx300m