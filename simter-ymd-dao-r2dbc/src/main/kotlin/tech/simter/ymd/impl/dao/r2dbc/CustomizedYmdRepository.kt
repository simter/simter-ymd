package tech.simter.ymd.impl.dao.r2dbc

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.impl.dao.r2dbc.po.YmdPo

/**
 * @author RJ
 */
interface CustomizedYmdRepository {
  fun save(entity: YmdPo): Mono<YmdPo>
  fun saveAll(entities: Iterable<YmdPo>): Flux<YmdPo>
  fun findYears(type: String): Flux<Int>
  fun findMonths(type: String, year: Int): Flux<Int>
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
}