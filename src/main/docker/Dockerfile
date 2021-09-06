# Notice: Run with the module root as context root, else you will be unable to copy the jar!

FROM adoptopenjdk/openjdk11
MAINTAINER midorlo.com
COPY dist/k9-lib-security.jar ./k9-lib-security.jar
ENTRYPOINT ["java","-jar","/k9-lib-security.jar"]