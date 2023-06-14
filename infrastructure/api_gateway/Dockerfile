#FROM java:8
FROM x/jre:v1.0
ADD ./target/api_gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "app.jar","-Xms32M -Xmx32M -XX:PermSize=32M -XX:MaxPermSize=64M"]