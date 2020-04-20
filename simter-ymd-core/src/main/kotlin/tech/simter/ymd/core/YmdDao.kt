package tech.simter.ymd.core

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * The Dao Interface.
 *
 * This interface should only be use by [YmdService]. It is design to public just for multiple Dao implements.
 *
 * @author RJ
 */
interface YmdDao {
  /**
   * Create all the given [ymd] instance.
   *
   * Result the mono signal when create successful.
   */
  fun create(vararg ymd: Ymd): Mono<Void>

  /**
   * Find all years of the specific [type].
   *
   * The result is ordered by year desc.
   */
  fun findYears(type: String): Flux<Int>

  /**
   * Find all months of the specific [type] and [year].
   *
   * The result is ordered by month desc.
   */
  fun findMonths(type: String, year: Int): Flux<Int>

  /**
   * Find all days of the specific [type], [year] and [month].
   *
   * The result is ordered by day desc.
   */
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
}