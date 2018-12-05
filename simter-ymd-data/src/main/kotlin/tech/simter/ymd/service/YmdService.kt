package tech.simter.ymd.service

import reactor.core.publisher.Flux
import tech.simter.ymd.dto.YearNode
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.time.YearMonth

/**
 * The Service Interface.
 *
 * This interface is design for all external modules to use.
 *
 * @author RJ
 */
interface YmdService {
  /**
   * Find all years of the specific [type].
   *
   * The result is ordered by year desc.
   */
  fun findYears(type: String): Flux<Year>

  /**
   * Find all months of the specific [type] and [year].
   *
   * The result is ordered by month desc.
   */
  fun findMonths(type: String, year: Year): Flux<Month>

  /**
   * Find all
   * days of the specific [type] and [yearMonth].
   *
   * The result is ordered by day desc.
   */
  fun findDays(type: String, yearMonth: YearMonth): Flux<MonthDay>

  /**
   * Find all years of the specific [type].
   *
   * Also find all months of the latest year if it exists.
   * Then if the latest year has one month at least, continue find all days of the latest month.
   *
   * The result is ordered by year desc, month desc, day desc.
   */
  fun findYearsWithLatestDay(type: String): Flux<YearNode>
}