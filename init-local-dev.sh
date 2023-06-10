#!/bin/sh

## check /docker directory
####################################################
BASE_DIR="$( cd "$( dirname "$0" )" && pwd )"
echo $BASE_DIR
DOCKER_DIR="$BASE_DIR/docker"
if [ -d "$DOCKER_DIR" ]; then
  echo "/docker directory is already exists!"
else
  echo "mkdir new /docker directory"
  mkdir "docker"
fi

MYSQL_DIR="$DOCKER_DIR/mysql"
if [ -d "$MYSQL_DIR" ]; then
  echo "/docker/mysql directory is already exists!"
else
  echo "mkdir new /docker/mysql directory"
  mkdir "docker/mysql"
fi

#REDIS_DIR="$DOCKER_DIR/redis"
#if [ -d "$REDIS_DIR" ]; then
#  echo "/docker/redis directory is already exists!"
#else
#  echo "mkdir new /docker/redis directory"
#  mkdir "docker/redis"
#fi
## check /docker directory finished
####################################################


## init mysql container
####################################################
DOCKER_NAME=mysql-wave
PORT=43306
MYSQL_ROOT_PASSWORD=develop
 
containerExists=$(docker ps -a | grep ${DOCKER_NAME})
 
echo ${containerExists}

if [ -z "${containerExists}" ]; then
  echo "${DOCKER_NAME} container is empty, running new container..."
  docker run --name $DOCKER_NAME -p $PORT:3306 -v `pwd`/docker/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD -d mysql:8
  exit 1
fi

echo "${DOCKER_NAME} container is already exists, restarting container..."
docker stop $DOCKER_NAME
docker start ${DOCKER_NAME}
## init mysql container finished
####################################################
