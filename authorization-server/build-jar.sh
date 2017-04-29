#!/usr/bin/env bash
mvn clean package
#java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8888 --port 7888 --noCluster --outputUberJar target/authorization-server.jar
java -jar ~/Java/ap-server/payara-microprofile-1.0-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8888 --port 7888 --noCluster --outputUberJar target/authorization-server.jar
java -jar target/authorization-server.jar --sslport 8888