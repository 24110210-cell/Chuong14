# Dùng JDK 21 LTS có sẵn, ổn định và không cần tải JDK 26 preview
FROM tomcat:10.1-jdk21-openjdk-slim

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR vào thư mục webapps
COPY dist/Bai13_1.war /usr/local/tomcat/webapps/ROOT.war

# QUAN TRỌNG: Thay đổi cổng Tomcat lắng nghe thành biến $PORT của Vercel
# Vercel route traffic đến port 80 mặc định, Tomcat phải lắng nghe port này
ENV PORT=8080
RUN sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml

# Expose cổng (chỉ mang tính tài liệu, Vercel quản lý cổng tự động)
EXPOSE 8080

# Khởi động Tomcat
CMD ["catalina.sh", "run"]