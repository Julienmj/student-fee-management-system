FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . /app

# NetBeans Ant build output lands in dist/
# Expect Oracle JDBC driver at lib/ojdbc17.jar (copied with source tree)
CMD ["bash", "-lc", "ant clean jar && java -jar dist/StudentFeesTrackerApp.jar"]

