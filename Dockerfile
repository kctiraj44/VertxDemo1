FROM adoptopenjdk:11-jre-hotspot

ENV FAT_JAR vertex-starter-1.0.0-SNAPSHOT-fat.jar

ENV APP_HOME /usr/app

EXPOSE 8888

COPY build/libs/$FAT_JAR $APP_HOME/

WORKDIR $APP_HOME

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
