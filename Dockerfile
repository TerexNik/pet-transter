# the first stage of our build will extract the layers
FROM bellsoft/liberica-openjdk-alpine:17 as builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# the second stage of our build will copy the extracted layers
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR workspace
COPY --from=builder /workspace/dependencies/ ./
RUN true
COPY --from=builder /workspace/spring-boot-loader/ ./
RUN true
COPY --from=builder /workspace/snapshot-dependencies/ ./
RUN true
COPY --from=builder /workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
