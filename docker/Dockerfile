FROM openjdk:8

MAINTAINER DAE
LABEL authors="yifanzheng"
COPY sso-admin-service-1.0.0.jar /opt/sso-admin-service/
WORKDIR /opt/sso-admin-service

EXPOSE 8401

CMD ["java", "-server", "-jar", "sso-admin-service-1.0.0.jar", \
  "--spring.profiles.active=dev", \
  "--jasypt.encryptor.password=SSO@DEV",\
  "--server.port=8401"]