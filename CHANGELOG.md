# simter-ymd changelog

## 0.9.0 2020-04-20

- Rename `YmdService|Dao.save(...)` to `.create(...)` - this is a broken api change
- Add integration test for rest-api
- User `DatabaseClient` instead of `ConnectionFactory` to implement `YmdDao` on dao-r2dbc module
- Initial `simter-ymd-test` module for unit test
- Polishing jpql to compatible with EclipseLink
- Add mssql SQL
- Add mysql SQL
- Add hsql SQL
- Add derby SQL
- Polishing h2 and postgres SQL
- Fixed compile error after upgrade
- Upgrade to simter-1.3.0-M14 (kotlin-1.3.70+)

## 0.8.0 2020-04-17

- Log main config property on the starter with info level
- Rename property 'module.rest-context-path.simter-ymd' to 'simter-ymd.rest-context-path'
- Polishing unit test code to avoid deprecated warning
- Rename property 'module.authorization.simter-ymd' to 'simter-ymd.authorization'
- Polishing Maven module config
- Upgrade to simter-1.3.0-M13

## 0.7.0 2019-09-12

- Rename table `st_ymd` columns `type, year, month, day` to `t, y , m, d`

> Execute script [postgres\upgrade-0.3.0-0.7.0.sql] to do the upgrade.

[postgres\upgrade-0.3.0-0.7.0.sql]: 

## 0.6.0 2019-09-11

- Upgrade to simter-1.2.0
- Use stable spring version on main branch
- Use milestone spring version for r2dbc
- Support jpa-eclipselink-x profiles on starter module

## 0.5.0 2019-06-18

- Upgrade to simter-1.2.0-M6
- change unit test code to use MockK and SpringMockK
- Support use ModuleAuthorizer to verify permission when call service method
- Rename groupId to tech.simter.ymd
- Refactor module structure to make core api simplify and clear. [#5](https://github.com/simter/simter-ymd/issues/5)
- Wrap block dao implementation to reactor scheduler. [#4](https://github.com/simter/simter-ymd/issues/4)

## 0.4.0 2019-01-14

- Upgrade to simter-build-1.1.0 and simter-dependencies-1.1.0

## 0.3.0 2019-01-10

- Upgrade to simter platform 1.0.0

## 0.2.0 2019-01-03

- Change id to String type, not auto generate, default uid
- Implement simter-ymd-data-reactive-mongo
- Implement simter-ymd-data-r2dbc
- Refactor starter's maven profiles
- Add new rest 'POST /' for create a Ymd record
- Add new rest 'POST /{type}' for batch create multiple Ymd records
- Change default profile to `dev` and `reactive-mongo-embedded`

## 0.1.0 2018-12-28

- Initial base on simter-0.7.0