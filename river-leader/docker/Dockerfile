#基于 java:v8 来构建我们的镜像
FROM 172.16.4.110:1500/library/java:v8

#维护者
MAINTAINER app="smardcloud.com river-leader" version="1.0.0" by="jiangjie"
#LABEL app="smardcloud.com river-leader" version="1.0.0" by="jiangjie"

#声明变量 在docker build创建镜像时 使用--build-arg 传入参数source
#ARG source

#设置工作目录为 `/publish` 文件夹，即容器启动默认的文件夹
#WORKDIR /workflow

#拷贝项目到docker容器中,将宿主机的river-leader-1.0.0.jar拷贝到容器的/opt/jar/目录下
ADD ./river-leader-1.0.0.jar /opt/jar/river-leader.jar

#设置Docker容器对外暴露端口
EXPOSE 8888

#使用来运行应用程序
ENTRYPOINT ["java", "-jar", "/opt/jar/river-leader.jar","--server.port=8888"]
#CMD java -jar river-leader.jar --server.port=8888
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]