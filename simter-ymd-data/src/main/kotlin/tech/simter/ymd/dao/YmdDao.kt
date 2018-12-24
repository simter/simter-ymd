package tech.simter.ymd.dao

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.po.Ymd
import tech.simter.ymd.service.YmdService

/**
 * The Dao Interface.
 *
 * This interface should only be use by [YmdService]. It is design to public just for multiple Dao implements.
 *
 * @author RJ
 */
interface YmdDao {
  /**
   * Save or update all the given [ymd] entity.
   *
   * Result the mono signal when save successful.
   */
  fun save(vararg ymd: Ymd): Mono<Void>

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