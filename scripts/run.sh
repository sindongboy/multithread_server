#!/bin/bash

#--------------#
# start server
#--------------#

RUNTYPE=""
PORT=""

# help 
function usage() {
	echo "Usage: $0 [options]"
	echo "options:"
	echo "-h, --help                show help"
	echo "-t, --type=[SERVER|CLIENT]      specify run type"
	echo "-p, --port=[SERVER|CLIENT]      specify port"
	exit 0
}

if [ $# == 0 ]; then
	usage
fi

while test $# -gt 0; do
	case "$1" in
		-h|--help)
			usage
			;;
		-t)
			shift
			if test $# -gt 0; then
				if [[ ! $1 == "SERVER" && ! $1 == "CLIENT" ]]; then
					echo "[ERROR] Run type must be either SERVER or CLIENT"
					usage
				fi
				RUNTYPE=$1
			else
				echo "no run type specified, must be either SERVER or CLIENT "
				exit 1
			fi
			shift
			;;
		--type*)
			RUNTYPE=`echo $1 | sed -e 's/^[^=]*=//g'`
			if [[ ! ${RUNTYPE} == "SERVER" && ! ${RUNTYPE} == "CLIENT" ]]; then
				echo "[ERROR] Run type must be either SERVER or CLIENT"
				usage
			fi
			shift
			;;
		-p)
			shift
			if test $# -gt 0; then
				PORT=$1
			else
				echo "no port number specified"
				exit 1
			fi
			shift
			;;
		--port*)
			PORT=`echo $1 | sed -e 's/^[^=]*=//g'`
			shift
			;;
		*)
			break
			;;
	esac
done

if [ -z ${RUNTYPE} ]; then
	echo "Run type not specified"
	usage
fi

if [ -z ${PORT} ]; then
	echo "Port number not specified"
	usage
fi

# Environment
PACKAGE="com.skplanet.nlp"

# dependencies
LOG4J="/Users/sindongboy/.m2/repository/log4j/log4j/1.2.7/log4j-1.2.7.jar"
TARGET="../target/multithread-server-1.0.0-SNAPSHOT.jar"

# running
CP="-classpath ./:${TARGET}:${LOG4J}"
if [ ${RUNTYPE} == "SERVER" ]; then
	java ${CP} ${PACKAGE}.server.Server ${PORT}
else
	java ${CP} ${PACKAGE}.client.Client ${PORT}
fi
