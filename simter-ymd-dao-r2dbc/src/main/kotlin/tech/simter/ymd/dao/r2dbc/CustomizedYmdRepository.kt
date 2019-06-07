package tech.simter.ymd.dao.r2dbc

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.po.Ymd

/**
 * @author RJ
 */
interface CustomizedYmdRepository {
  fun save(entity: Ymd): Mono<Ymd>
  fun saveAll(entities: Iterable<Ymd>): Flux<Ymd>
  fun findYears(type: String): Flux<Int>
  fun findMonths(type: String, year: Int): Flux<Int>
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
}