#!/usr/bin/env bash
mvn clean package
#java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8888 --port 7888 --noCluster
java -jar ~/Java/ap-server/payara-microprofile-1.0-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8888 --port 7888 --noCluster