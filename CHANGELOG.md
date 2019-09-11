# simter-ymd changelog

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