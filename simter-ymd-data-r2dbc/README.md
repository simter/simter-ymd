# simter-ymd-data-r2dbc

The [YmdDao] implementation by [R2DBC].

For running unit test, you need to :

1. Create a empty postgres database with name 'test_st_ymd' and owner to 'test' with login password 'password'. (You can change this by properties of [R2dbcProperties])
2. Run sql script file '[sql/postgres/schema-create.sql]'.
3. Run 'mvn test'.

[R2DBC]: https://r2dbc.io
[YmdDao]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-data/src/main/kotlin/tech/simter/ymd/dao/YmdDao.kt
[sql/postgres/schema-create.sql]: https://github.com/simter/simter-ymd/blob/master/simter-ymd-data/src/main/resources/tech/simter/ymd/sql/postgres/schema-create.sql
[R2dbcProperties]: https://github.com/simter/simter-r2dbc-ext/blob/master/src/main/kotlin/tech/simter/r2dbc/R2dbcProperties.kt
