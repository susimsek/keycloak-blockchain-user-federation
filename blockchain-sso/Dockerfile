FROM koosiedemoer/netty-tcnative-alpine

MAINTAINER suayb simsek suaybsimsek58@gmail.com

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/myservice/myservice.jar

ENTRYPOINT ["java", "-jar", "/usr/share/myservice/myservice.jar"]