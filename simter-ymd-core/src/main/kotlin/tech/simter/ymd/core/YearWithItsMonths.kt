package tech.simter.ymd.core

import kotlinx.serialization.Serializable

/**
 * The encapsulation for year and its months.
 *
 * @author RJ
 */
@Serializable
data class YearWithItsMonths(
  /** The 4 digits year value */
  val year: Int,
  /** The year's months, month value is from 1 to 12, order by months desc */
  val months: List<Int>? = null,
)