# simter-ymd

A Date Management for Massive Data.

## Maven Modules

| Sn | Name                          | Type | Parent                        | Remark
|----|-------------------------------|------|-------------------------------|--------
| 1  | [simter-ymd]            | pom  | [simter-build]                | Build these modules and define global properties and pluginManagement
| 2  | simter-ymd-bom          | pom  | simter-ymd              | Bom
| 3  | simter-ymd-parent       | pom  | simter-ymd              | Define global dependencies and plugins
| 4  | simter-ymd-core         | jar  | simter-ymd-parent       | Core API: [Ymd], [YmdDao] and [YmdService]
| 5  | simter-ymd-dao-mongo    | jar  | simter-ymd-parent       | [YmdDao] Implementation By Reactive MongoDB
| 6  | simter-ymd-dao-jpa      | jar  | simter-ymd-parent       | [YmdDao] Implementation By R2DBC
| 7  | simter-ymd-rest-webflux | jar  | simter-ymd-parent       | [Rest API] Implementation By WebFlux
| 8  | simter-ymd-starter      | jar  | simter-ymd-parent       | Microservice Starter

## Requirement

- Maven 3.6+
- Kotlin 1.3+
- Java 8+
- Spring Framework 5.2+
- Spring Boot 2.2+
- Reactor 3.3+


[simter-build]: https://github.com/simter/simter-build
[simter-ymd]: https://github.com/simter/simter-ymd
[Ymd]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/Ymd.kt
[YmdDao]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/YmdDao.kt
[YmdService]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-core/src/main/kotlin/tech/simter/ymd/core/YmdService.kt
[Rest API]: ./docs/rest-api.md