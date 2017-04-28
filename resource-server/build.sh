#!/usr/bin/env bash
mvn clean package
java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8070 --port 7070 --noCluster