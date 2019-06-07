package tech.simter.ymd.dto

/**
 * The [YearToMonthNode] owners some [MonthToDayNode].
 *
 * @author RJ
 */
data class YearToMonthNode(
  val year: Int, // 4 digits
  val months: List<Int>? = null // 1 to 12
)