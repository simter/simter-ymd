package tech.simter.ymd.impl.dao.mongo

import reactor.core.publisher.Flux

/**
 * @author RJ
 */
interface YmdRepositoryByTemplate {
  fun findYears(type: String): Flux<Int>
  fun findMonths(type: String, year: Int): Flux<Int>
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
}