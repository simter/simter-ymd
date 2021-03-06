# simter-ymd-dao-r2dbc

The [YmdDao] implementation by [R2DBC].

## Unit test

Run below command to test the compatibility with different embedded database:

```
mvn test -P embedded-h2 \
&& mvn test -P embedded-postgres \
&& mvn test -P embedded-mysql
```

If want to run test on host database, manual run below command:

```
mvn test -P postgres \
&& mvn test -P mysql \
&& mvn test -P mssql
```

> Could change the host database connection params through below `Maven Properties`.

## Maven Properties

| Property Name | Default Value | Remark                    |
|---------------|---------------|---------------------------|
| db.host       | localhost     | Database host             |
| db.name       | testdb        | Database name             |
| db.username   | tester        | Database connect username |
| db.password   | password      | Database connect password |

Use `-D {property-name}={property-value}` to override default value. Such as:

```bash
mvn test -D db.name=testdb
```

## Maven Profiles:

| Name              | Default | Supported |
|-------------------|:-------:|:---------:|
| embedded-h2       |    √    |     √     |
| embedded-postgres |         |     √     |
| postgres          |         |     √     |
| embedded-mysql    |         |     √     |
| mysql             |         |     √     |
| mssql             |         |     √     |

The default profile is `embedded-h2`.
Use `-P {profile-name}` to override default. Such as:

```bash
mvn test -P {profile-name}
```

> `embedded-postgres` depends on module [simter-embedded-database-ext].


[R2DBC]: https://r2dbc.io
[YmdDao]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/YmdDao.kt
[simter-embedded-database-ext]: https://github.com/simter/simter-embedded-database-ext