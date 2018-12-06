package tech.simter.ymd.dto

import java.time.MonthDay

/**
 * The [MonthToDayNode] owners some [MonthDay].
 *
 * @author RJ
 */
data class MonthToDayNode(
  val month: Int, // 1 to 12
  val days: List<Int> = listOf() // 1 to 31
)