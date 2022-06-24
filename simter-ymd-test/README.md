# simter-ymd-test

## 1. Unit test tools [TestHelper.kt]

A unit test tools for generate random value:

- `fun randomYmd(type, year, month, day): Ymd`
- `fun randomType(): String`
- `fun randomYear(): Int`
- `fun randomMonth(): Int`
- `fun randomDay(): Int`

## 2. Integration test

### 2.1. Start server

For test purpose, start the test server:

```shell
$ cd ../simter-ymd-starter
$ mvn clean spring-boot:run
```

> Ignore this if test on another server.

### 2.2. Run integration test on server

```shell
$ cd ../simter-ymd-test
$ mvn clean test -P integration-test
```

This will run all the integration test on each rest-api define in <[rest-api.md]>.

Want to run the integration test on the real server, just add specific param:

| ParamName  | Remark         | Default value             |
|------------|----------------|---------------------------|
| server.url | server address | http://127.0.0.1:8084/ymd |

[TestHelper.kt]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-test/src/main/kotlin/tech/simter/ymd/test/TestHelper.kt
[rest-api.md]: https://github.com/simter/simter-ymd/blob/master/docs/rest-api.md
