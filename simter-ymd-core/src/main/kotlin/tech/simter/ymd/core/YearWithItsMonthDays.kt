package tech.simter.ymd.core

import kotlinx.serialization.Serializable

/**
 * The encapsulation for year and its months and the month's days.
 *
 * @author RJ
 */
@Serializable
data class YearWithItsMonthDays(
  /** The 4 digits year value */
  val year: Int,
  /**
   * The year's months and the month's days.
   * Month value is from 1 to 12.
   * Day value is from 1 to 31.
   * Order by months desc and days desc
   */
  val months: List<MonthWithItsDays>? = null,
)