#!/usr/bin/env bash
mvn clean package
#java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8888 --port 7888 --noCluster --outputUberJar target/read-client.jar
java -jar ~/Java/ap-server/payara-microprofile-1.0-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8080 --port 7080 --noCluster --outputUberJar target/read-client.jar
open -a "/Applications/Google Chrome.app" https://localhost:8080/api/todo/index
java -jar target/read-client.jar --sslport 8080