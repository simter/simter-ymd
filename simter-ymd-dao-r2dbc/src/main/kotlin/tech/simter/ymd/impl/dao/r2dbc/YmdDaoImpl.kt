package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd
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
    return ymd.toFlux()
      .flatMap { create(it) }
      .then()
  }

  private fun create(ymd: Ymd): Mono<Int> {
    return databaseClient.insert()
      .into(TABLE_YMD)
      .value("t", ymd.type)
      .value("y", ymd.year)
      .value("m", ymd.month)
      .value("d", ymd.day)
      .value("id", Ymd.uid(
        type = ymd.type,
        year = ymd.year,
        month = ymd.month,
        day = ymd.day
      ))
      .fetch()
      .rowsUpdated()
  }

  override fun findYears(type: String): Flux<Int> {
    return databaseClient
      .execute("select distinct y from $TABLE_YMD" +
        " where t = :type" +
        " order by y desc")
      .bind("type", type)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return databaseClient
      .execute("select distinct m from $TABLE_YMD" +
        " where t = :type and y = :year" +
        " order by m desc")
      .bind("type", type)
      .bind("year", year)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return databaseClient
      .execute("select distinct d from $TABLE_YMD" +
        " where t = :type and  y = :year and m = :month" +
        " order by d desc")
      .bind("type", type)
      .bind("year", year)
      .bind("month", month)
      .map { row -> row.get(0, Short::class.javaObjectType)!!.toInt() }
      .all()
  }
}