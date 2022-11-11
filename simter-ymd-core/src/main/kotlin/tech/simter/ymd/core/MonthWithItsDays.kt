package tech.simter.ymd.core

import kotlinx.serialization.Serializable

/**
 * The encapsulation for month and its days.
 *
 * @author RJ
 */
@Serializable
data class MonthWithItsDays(
  /** The month value, from 1 to 12 */
  val month: Int,
  /** The month's days, from 1 to 31, order by days desc */
  val days: List<Int>? = null
)
