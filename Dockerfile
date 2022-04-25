FROM openjdk:17-alpine

RUN mkdir src
WORKDIR /src
COPY ./exchangeproxy/target/exchangeproxy-1.0-jar-with-dependencies.jar .
COPY .env .
ENTRYPOINT [ "java" , "-cp", "exchangeproxy-1.0-jar-with-dependencies.jar", "com.exchange_proxy.App" ]