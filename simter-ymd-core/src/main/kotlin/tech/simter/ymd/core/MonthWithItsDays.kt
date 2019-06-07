package tech.simter.ymd.core

/**
 * The encapsulation for month and its days.
 *
 * @author RJ
 */
interface MonthWithItsDays {
  /** The month value, from 1 to 12 */
  val month: Int
  /** The month's days, from 1 to 31, order by days desc */
  val days: List<Int>?
}