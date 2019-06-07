package tech.simter.ymd.impl

import tech.simter.ymd.core.YearWithItsMonths

/**
 * The immutable implementation of [YearWithItsMonths].
 *
 * @author RJ
 */
data class ImmutableYearWithItsMonths(
  override val year: Int,
  override val months: List<Int>? = null
) : YearWithItsMonths