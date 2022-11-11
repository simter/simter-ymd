package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.Ymd.Companion.TABLE
import tech.simter.ymd.core.YmdDao

/**
 * The r2dbc implementation of [YmdDao].
 *
 * @author RJ
 */
@Repository
class YmdDaoImpl @Autowired constructor(
  private val databaseClient: DatabaseClient
) : YmdDao {
  override fun create(vararg ymd: Ymd): Mono<Void> {
    return ymd.toFlux().flatMap { create(it) }.then()
  }

  private fun create(ymd: Ymd): Mono<Int> {
    return databaseClient.sql("insert into $TABLE(t, y, m, d, id) values (:t, :y, :m, :d, :id)")
      .bind("t", ymd.type)
      .bind("y", ymd.year)
      .bind("m", ymd.month)
      .bind("d", ymd.day)
      .bind(
        "id", Ymd.uid(
          type = ymd.type,
          year = ymd.year,
          month = ymd.month,
          day = ymd.day
        )
      )
      .fetch()
      .rowsUpdated()
  }

  override fun findYears(type: String): Flux<Int> {
    return databaseClient
      .sql("select distinct y from $TABLE where t = :type order by y desc")
      .bind("type", type)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return databaseClient
      .sql("select distinct m from $TABLE where t = :type and y = :year order by m desc")
      .bind("type", type)
      .bind("year", year)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return databaseClient
      .sql("select distinct d from $TABLE where t = :type and  y = :year and m = :month order by d desc")
      .bind("type", type)
      .bind("year", year)
      .bind("month", month)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }
}