package tech.simter.ymd.dao.r2dbc

import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Mono
import tech.simter.ymd.po.Ymd

/**
 * See interfaces [ReactiveSortingRepository], [ReactiveQueryByExampleExecutor], [ReactiveCrudRepository], [DatabaseClient].
 *
 * See implements [SimpleR2dbcRepository].
 *
 * @author RJ
 */
interface YmdRepository : Repository<Ymd, String>, CustomizedYmdRepository {
  /*  TODO : Because spring-data-r2dbc(1.0.0.M1) do not support projection now,
      so need to use a custom impl by ConnectionFactory through YmdRepositoryByConnectionFactory.
      See [spring-data-r2dbc/issues#41](https://github.com/spring-projects/spring-data-r2dbc/issues/41)
      Error : org.springframework.data.mapping.MappingException: Couldn't find PersistentEntity for type class java.lang.Integer!

  @Query("select distinct year from $TABLE_NAME where type = ?1 order by year desc")
  fun findYears(type: String): Flux<Int>

  @Query("select distinct month from $TABLE_NAME where type = ?1 and year = ?2 order by month desc")
  fun findMonths(type: String, year: Int): Flux<Int>

  @Query("select distinct day from $TABLE_NAME where type = ?1 and  year = ?2 and month = ?3 order by day desc")
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
  */

  // TODO : bellow standard methods is copied from ReactiveCrudRepository. Need to delete after spring-data-r2dbc stabled
  fun findById(id: String): Mono<Ymd>
  fun deleteAll(): Mono<Void>
  fun count(): Mono<Long>
}