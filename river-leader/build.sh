# 定义变量
API_NAME="demo"
API_VERSION="0.0.1"
API_PORT=58080
IMAGE_NAME="127.0.0.1:5000/billjiang/$API_NAME:$BUILD_NUMBER"
CONTAINER_NAME=$API_NAME-$API_VERSION

# 进入target 目录复制Dockerfile 文件
cd $WORKSPACE/target
cp classes/Dockerfile .

#构建docker 镜像
docker build -t $IMAGE_NAME .

#推送docker镜像
docker push $IMAGE_NAME

#删除同名docker容器
cid=$(docker ps | grep "$CONTAINER_NAME" | awk '{print $1}')
if [ "$cid" != "" ]; then
   docker rm -f $cid
fi

#启动docker 容器
docker run -d -p $API_PORT:8080 --name $CONTAINER_NAME $IMAGE_NAME

#删除 Dockerfile 文件
rm -f Dockerfile



#!/bin/bash

source /etc/profile

MYIMAGE=192.168.1.2:8082/springboot/springboot-jpa

# uncomment if you need push
#docker login 192.168.1.2:8082 -u admin -p admin123

# stop all container
docker kill $(docker ps -aq)

# remove all container
docker rm $(docker ps -aq)

# remove old images
docker images | grep 192.168.1.2:8082/springboot/springboot-jpa | awk '{print $3}' | xargs docker rmi

# build jar and image
mvn package -e -X docker:build -DskipTest

# running container
docker run -dp 8080:8080 --name springboot-jpa ${MYIMAGE}

# push image
#docker push ${MYIMAGE}