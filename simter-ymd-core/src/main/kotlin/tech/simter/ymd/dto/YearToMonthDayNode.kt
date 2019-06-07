package tech.simter.ymd.dto

/**
 * The [YearToMonthDayNode] owners some [MonthToDayNode].
 *
 * @author RJ
 */
data class YearToMonthDayNode(
  val year: Int,
  val months: List<MonthToDayNode>? = null
)