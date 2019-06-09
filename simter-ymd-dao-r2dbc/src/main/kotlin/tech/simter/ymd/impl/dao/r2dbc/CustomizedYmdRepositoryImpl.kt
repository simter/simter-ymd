package tech.simter.ymd.impl.dao.r2dbc

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.impl.dao.r2dbc.po.YmdPo

/**
 * @author RJ
 */
@Repository
class CustomizedYmdRepositoryImpl @Autowired constructor(
  private val connectionFactory: ConnectionFactory
) : CustomizedYmdRepository {
  private val logger = LoggerFactory.getLogger(CustomizedYmdRepository::class.java)

  private fun connection(): Mono<Connection> {
    return Mono.from(connectionFactory.create())
  }

  override fun save(entity: YmdPo): Mono<YmdPo> {
    return this.connection()
      .flatMapMany { c ->
        c.createStatement("insert into $TABLE_YMD(id, type, year, month, day) values($1, $2, $3, $4, $5)")
          .bind("$1", entity.id)
          .bind("$2", entity.type)
          .bind("$3", entity.year)
          .bind("$4", entity.month)
          .bind("$5", entity.day)
          .add()
          .execute()
      }.then(Mono.just(entity))
  }

  override fun saveAll(entities: Iterable<YmdPo>): Flux<YmdPo> {
    return Flux.fromIterable(entities).concatMap { this.save(it) }
  }

  override fun findYears(type: String): Flux<Int> {
    return this.connection()
      .flatMapMany { c ->
        Flux.from(
          c.createStatement("select distinct year from $TABLE_YMD" +
            " where type = $1 order by year desc")
            .bind("$1", type)
            .execute()
          // TODO : Cannot decode value of type java.lang.Integer
          //).flatMap { it.map { row, _ -> row.get("year", Integer::class.java)?.toInt() } }
        ).flatMap { it.map { row, _ -> row.get("year")?.toString()?.toInt() } }
      }
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return this.connection()
      .flatMapMany { c ->
        Flux.from(
          c.createStatement("select distinct month from $TABLE_YMD" +
            " where type = $1 and year = $2 order by month desc")
            .bind("$1", type)
            .bind("$2", year)
            .execute()
        ).flatMap { it.map { row, _ -> row.get("month")?.toString()?.toInt() } }
      }
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    logger.debug("type={}, year={}, month={}", type, year, month)
    return this.connection()
      .flatMapMany { c ->
        Flux.from(
          c.createStatement("select distinct day from $TABLE_YMD" +
            " where type = $1 and year = $2 and month = $3 order by day desc")
            .bind("$1", type)
            .bind("$2", year)
            .bind("$3", month)
            .execute()
        ).flatMap { it.map { row, _ -> row.get("day")?.toString()?.toInt() } }
      }
  }
}