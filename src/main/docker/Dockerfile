FROM openjdk:8-jre-slim-stretch

ENV FLATPAK_GL_DRIVERS=dummy

# Install flatpak from stretch-backports add the flathub repository
# Run `docker build --no-cache .` to update dependencies
RUN printf "deb http://ftp.debian.org/debian stretch-backports main" > /etc/apt/sources.list.d/backports.list && \
  apt-get update && apt-get install --no-install-recommends -y -t stretch-backports flatpak && \
  flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

#Mount /tmp folder for Tomcat temp files
VOLUME /tmp

#Add latest build jar file
ADD target/linux-store-backend-0.6.4.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]