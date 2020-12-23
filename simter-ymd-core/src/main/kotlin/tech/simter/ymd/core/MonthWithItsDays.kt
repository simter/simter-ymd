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

  /** An inner immutable [MonthWithItsDays] implementation */
  private data class Impl(
    override val month: Int,
    override val days: List<Int>? = null
  ) : MonthWithItsDays

  companion object {
    /** Create an immutable [Ymd] instance */
    fun of(month: Int, days: List<Int>? = null): MonthWithItsDays {
      return Impl(month, days)
    }
  }
}