package tech.simter.ymd.dto

import java.time.Year

/**
 * The [YearNode] owners some [MonthNode].
 *
 * @author RJ
 */
data class YearNode(
  val year: Year,
  val months: List<MonthNode>?
)