#!/bin/bash


DEP=`find ../lib -type f -name "*.jar" | awk '{printf("%s:", $0);}' | sed 's/:$//g'`
TARGET="../target/multithread-server-1.0.0-SNAPSHOT.jar"
DRIVER="com.skplanet.nlp.client.RestfulClient"


CP="${DEP}:${TARGET}"

java -Xmx4G -cp ${CP} ${DRIVER}


