# Simter YMD Modules

A module for manage massive data date.

## Requirement

- Maven 3.6+
- Kotlin 1.3+
- Java 8+
- Spring Framework 5+
- Spring Boot 2+
- Reactor 3+

## Maven Modules

Sn | Name                             | Parent                  | Remark
---|----------------------------------|-------------------------|--------
1  | [simter-ymd-build]               | [simter-build:0.7.0]    | Build modules and define global properties and pluginManagement
2  | [simter-ymd-dependencies]        | simter-ymd-build        | Define global dependencyManagement
3  | [simter-ymd-parent]              | simter-ymd-dependencies | All sub modules parent module, Define global dependencies and plugins
4  | [simter-ymd-data]                | simter-ymd-parent       | Define Service and Dao Interfaces
5  | [simter-ymd-data-reactive-mongo] | simter-ymd-parent       | Dao Implementation By Reactive MongoDB
6  | [simter-ymd-data-jpa]            | simter-ymd-parent       | Dao Implementation By JPA
7  | [simter-ymd-rest-webflux]        | simter-ymd-parent       | Rest API By WebFlux
8  | [simter-ymd-starter]             | simter-ymd-parent       | Microservice Starter

> Remark : Module 1, 2, 3 all has maven-enforcer-plugin and flatten-maven-plugin config. Other modules must not configure them.


[simter-build:0.7.0]: https://github.com/simter/simter-build/tree/0.7.0
[simter-ymd-build]: https://github.com/simter/simter-ymd
[simter-ymd-dependencies]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-dependencies
[simter-ymd-parent]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-parent
[simter-ymd-data]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-data
[simter-ymd-data-jpa]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-data-jpa
[simter-ymd-data-reactive-mongo]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-data-reactive-mongo
[simter-ymd-rest-webflux]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-rest-webflux
[simter-ymd-starter]: https://github.com/simter/simter-ymd/tree/master/simter-ymd-starter