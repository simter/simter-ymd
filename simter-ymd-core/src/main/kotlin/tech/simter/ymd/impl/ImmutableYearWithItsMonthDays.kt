package tech.simter.ymd.impl

import tech.simter.ymd.core.MonthWithItsDays
import tech.simter.ymd.core.YearWithItsMonthDays

/**
 * The immutable implementation of [YearWithItsMonthDays].
 *
 * @author RJ
 */
data class ImmutableYearWithItsMonthDays(
  override val year: Int,
  override val months: List<MonthWithItsDays>? = null
) : YearWithItsMonthDays