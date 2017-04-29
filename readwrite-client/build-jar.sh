#!/usr/bin/env bash
mvn clean package
#java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8081 --port 7081 --noCluster --outputUberJar target/readwrite-client.jar
java -jar ~/Java/ap-server/payara-microprofile-1.0-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8081 --port 7081 --noCluster --outputUberJar target/readwrite-client.jar
open -a "/Applications/Safari.app" https://localhost:8081/api/todo/index
java -jar target/readwrite-client.jar --sslport 8081