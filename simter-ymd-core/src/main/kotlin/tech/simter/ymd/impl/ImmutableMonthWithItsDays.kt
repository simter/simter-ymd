package tech.simter.ymd.impl

import tech.simter.ymd.core.MonthWithItsDays

/**
 * The immutable implementation of [MonthWithItsDays].
 *
 * @author RJ
 */
data class ImmutableMonthWithItsDays(
  override val month: Int,
  override val days: List<Int>? = null
) : MonthWithItsDays