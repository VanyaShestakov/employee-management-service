FROM tomcat:8-jre8

RUN echo "export JAVA_OPTS=\"-Dapp.env=employeestat\"" > /usr/local/tomcat/bin/setenv.sh
COPY employeestat.war /usr/local/tomcat/webapps/employeestat.war

CMD ["catalina.sh", "run"]