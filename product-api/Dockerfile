FROM maven:3.8.4-jdk-11
COPY . .
WORKDIR .
RUN mvn install -DskipTests
EXPOSE 8081:8081
CMD ["mvn", "spring-boot:run"]
