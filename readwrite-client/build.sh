#!/usr/bin/env bash
mvn clean package
open -a "/Applications/Google Chrome.app" https://localhost:8081/api/todo/index
java -jar ~/Java/ap-server/payara-micro-4.1.1.171.1.jar --deploy target/ROOT.war --sslport 8081 --port 7081 --noCluster