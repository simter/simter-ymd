package tech.simter.ymd.dto

import java.time.Month
import java.time.MonthDay

/**
 * The [MonthNode] owners some [MonthDay].
 *
 * @author RJ
 */
data class MonthNode(
  val month: Month,
  val days: List<MonthDay>?
)