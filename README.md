# simter-ymd

A Date Management for Massive Data.

## Maven Modules

| Sn  | Name                    | Type | Parent                | Remark                                                                |
|-----|-------------------------|------|-----------------------|-----------------------------------------------------------------------|
| 1   | [simter-ymd]            | pom  | [simter-dependencies] | Build these modules and define global properties and pluginManagement |
| 2   | simter-ymd-bom          | pom  | simter-ymd            | Bom                                                                   |
| 3   | simter-ymd-parent       | pom  | simter-ymd            | Define global dependencies and plugins                                |
| 4   | simter-ymd-core         | jar  | simter-ymd-parent     | Core API: [Ymd], [YmdDao] and [YmdService]                            |
| 5   | simter-ymd-test         | jar  | simter-ymd-parent     | Common unit test helper method                                        |
| 6   | simter-ymd-dao-r2dbc    | jar  | simter-ymd-parent     | [YmdDao] Implementation By R2DBC                                      |
| 7   | simter-ymd-dao-mongo    | jar  | simter-ymd-parent     | [YmdDao] Implementation By Reactive MongoDB                           |
| 8   | simter-ymd-dao-jpa      | jar  | simter-ymd-parent     | [YmdDao] Implementation By JPA                                        |
| 9   | simter-ymd-dao-web      | jar  | simter-ymd-parent     | TODO: [YmdDao] Implementation By WebFlux                              |
| 10  | simter-ymd-service-impl | jar  | simter-ymd-parent     | Default [YmdService] Implementation                                   |
| 11  | simter-ymd-rest-webflux | jar  | simter-ymd-parent     | [Rest API] Implementation By WebFlux                                  |
| 12  | simter-ymd-starter      | jar  | simter-ymd-parent     | Microservice Starter                                                  |

## Requirement

- Java 17+
- Maven 3.8+
- Spring Boot 2.7+
    - Spring Framework 5.3+
    - Kotlin 1.6+
    - Reactor 3.4+


[simter-dependencies]: https://github.com/simter/simter-dependencies
[simter-ymd]: https://github.com/simter/simter-ymd
[Ymd]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/Ymd.kt
[YmdDao]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/YmdDao.kt
[YmdService]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/YmdService.kt
[Rest API]: ./docs/rest-api.md