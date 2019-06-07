package tech.simter.ymd.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.dto.MonthToDayNode
import tech.simter.ymd.dto.YearToMonthDayNode
import tech.simter.ymd.dto.YearToMonthNode
import tech.simter.ymd.po.Ymd

/**
 * The Service Interface.
 *
 * This interface is design for all external modules to use.
 *
 * @author RJ
 */
interface YmdService {
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

  /**
   * Find all years of the specific [type].
   *
   * Also find all months of the latest year if it exists.
   *
   * The result is ordered by year desc, month desc, day desc.
   */
  fun findYearsWithLatestYearMonths(type: String): Flux<YearToMonthNode>

  /**
   * Find all months of the specific [type] and [year].
   *
   * Also find all days of the latest month if it exists.
   *
   * The result is ordered by month desc, day desc.
   */
  fun findMonthsWithLatestMonthDays(type: String, year: Int): Flux<MonthToDayNode>

  /**
   * Find all years of the specific [type].
   *
   * Also find all months of the latest year if it exists.
   * Then if the latest year has one month at least, continue find all days of the latest month.
   *
   * The result is ordered by year desc, month desc, day desc.
   */
  fun findYearsWithLatestMonthDays(type: String): Flux<YearToMonthDayNode>
}