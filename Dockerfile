#Tomcat 8

FROM tomcat:8-jdk8

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/spring-boot-app-blogpost.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

WORKDIR /usr/local/tomcat/
VOLUME /usr/local/tomcat/webapps