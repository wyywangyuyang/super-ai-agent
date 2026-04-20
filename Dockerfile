# 选择基础镜像
FROM maven:3.8-amazoncorretto-21
WORKDIR /app

# 复制代码到容器内
COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests


EXPOSE 8123

# 容器启动时运行 jar 包
CMD ["java", "-jar", "/app/target/super-ai-agent-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]