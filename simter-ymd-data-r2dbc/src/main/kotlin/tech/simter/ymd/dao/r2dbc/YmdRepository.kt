package tech.simter.ymd.dao.r2dbc

import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.repository.query.Query
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import tech.simter.ymd.TABLE_NAME
import tech.simter.ymd.po.Ymd

/**
 * See interfaces [ReactiveSortingRepository], [ReactiveQueryByExampleExecutor], [ReactiveCrudRepository], [DatabaseClient].
 *
 * See implements [SimpleR2dbcRepository].
 *
 * @author RJ
 */
interface YmdRepository : ReactiveCrudRepository<Ymd, Int> {
  // TODO : org.springframework.data.mapping.MappingException: Couldn't find PersistentEntity for type class java.lang.Integer!
  @Query("select distinct year from $TABLE_NAME where type = ?1 order by year desc")
  fun findYears(type: String): Flux<Int>

  @Query("select distinct month from $TABLE_NAME where type = ?1 and year = ?2 order by month desc")
  fun findMonths(type: String, year: Int): Flux<Int>

  @Query("select distinct day from $TABLE_NAME where type = ?1 and  year = ?2 and month = ?3 order by day desc")
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
}