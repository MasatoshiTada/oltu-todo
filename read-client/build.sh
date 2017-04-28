#!/usr/bin/env bash
mvn clean package
open -a "/Applications/Google Chrome.app" https://localhost:8080/api/todo/index
java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8080 --port 7080 --noCluster