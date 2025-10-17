FROM tomcat:jdk21-openjdk-slim

COPY target/*.war webapps/

# Copie du driver JDBC
COPY conf/mysql-connector-j-9.3.0.jar /usr/local/tomcat/lib/

# Copie du fichier context.xml pour activer le JNDI
COPY conf/context.xml /usr/local/tomcat/conf/context.xml

# Copie du fichier tomcat-users.xml pour accéder au manager app
COPY conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

RUN sed -i 's/^\(.*RemoteAddrValve.*\)$/<!-- \1 -->/' /usr/local/tomcat/webapps/manager/META-INF/context.xml
RUN sed -i 's/^\(.*RemoteAddrValve.*\)$/<!-- \1 -->/' /usr/local/tomcat/webapps/host-manager/META-INF/context.xml