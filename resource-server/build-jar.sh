#!/usr/bin/env bash
mvn clean package
#java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8070 --port 7070 --noCluster --outputUberJar target/resource-server.jar
java -jar ~/Java/ap-server/payara-microprofile-1.0-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8070 --port 7070 --noCluster --outputUberJar target/resource-server.jar
java -jar target/resource-server.jar --sslport 8070