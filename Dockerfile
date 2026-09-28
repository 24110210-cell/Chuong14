FROM tomcat:10.1-jdk21-openjdk-slim

# Tải JDK 26 và giải nén
RUN apt-get update && apt-get install -y wget && \
    wget https://download.oracle.com/java/26/latest/jdk-26_linux-x64_bin.tar.gz && \
    tar -xzf jdk-26_linux-x64_bin.tar.gz -C /usr/local/ && \
    rm jdk-26_linux-x64_bin.tar.gz

# Đổi tên thư mục vừa giải nén thành jdk-26 chuẩn
RUN mv /usr/local/jdk-26* /usr/local/jdk-26

# Khai báo JAVA_HOME chuẩn
ENV JAVA_HOME=/usr/local/jdk-26
ENV PATH=$JAVA_HOME/bin:$PATH

RUN rm -rf /usr/local/tomcat/webapps/*
COPY dist/Bai13_1.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]