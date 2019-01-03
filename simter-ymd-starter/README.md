#  Simter YMD Server

## Requirement

- Maven 3.6+
- Kotlin 1.3+
- Java 8+
- Spring Framework 5+
- Spring Boot 2+
- Reactor 3+

## Maven Profiles

| Environment | Profile                 | Persistence        | Remark
|-------------|-------------------------|--------------------|--------
| Development | dev                     |                    | db.name=test_st_ymd, db.username=test, db.password =password
| Production  | prod                    |                    | db.name=st_ymd, db.username=test, db.password =password
|             | reactive-mongo-embedded | [MongoDB]          | Reactive
|             | reactive-mongo          | [MongoDB]          | Reactive
|             | reactive-postgres       | [PostgreSQL]       | Reactive
|             | jpa-hsql                | [HyperSQL]         | JPA
|             | jpa-postgres            | [PostgreSQL]       | JPA

The default profile is `dev` and `reactive-mongo-embedded`. Run bellow command to start:

```bash
mvn spring-boot:run -P {profile-name}
```

Default server-port is 8084, use `-D port=8084` to change to another port.

## Maven Properties

Property Name | Default Value  | Remark
--------------|----------------|--------
port          | 8084           | Web server port
app.env       | dev            | Environment
db.host       | localhost      | Database host
db.platform   | mongo-embedded | `hsql`, `h2`, `postgres`, `mongo` or `mongo-embedded`
db.name       | test_st_ymd    | Database name
db.username   | test           | Database connect username
db.password   | password       | Database connect password
db.init-mode  | always         | Init database by `spring.datasource.schema/data` config. `never` or `always`
jpa.ddl-auto  | create-drop    | Init database by `spring.datasource.schema/data` config. `none` or `create-drop`

Use `-D {property-name}={property-value}` to override default value. Such as:

```bash
mvn spring-boot:run -D port=8084
```

## Build Production

```bash
mvn clean package -P prod,reactive-postgres
```

## Run Production

```bash
java -jar {package-name}.jar

# or
nohup java -jar {package-name}.jar > /dev/null &
```

## Run Integration Test

Run test in the real server.

1. Start server. Such as `mvn spring-boot:run`
2. Run [IntegrationTest.kt]


[Embedded MongoDB]: https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo#embedded-mongodb
[MongoDB]: https://www.mongodb.com
[HyperSQL]: http://hsqldb.org
[PostgreSQL]: https://www.postgresql.org
[IntegrationTest.kt]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-starter/src/test/kotlin/tech/simter/ymd/starter/IntegrationTest.kt