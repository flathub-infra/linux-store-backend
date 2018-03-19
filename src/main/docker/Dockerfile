FROM frolvlad/alpine-oraclejdk8:slim
#Mount /tmp folder for Tomcat temp files
VOLUME /tmp
#Add latest build jar file
ADD linux-store-backend-0.3.1.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
