package tech.simter.ymd.impl.service

import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.impl.ImmutableYmd

/**
 * Provide public method for test.
 *
 * @author RJ
 */
object TestHelper {
  /** Generate a random [Ymd] instance */
  fun randomYmd(
    type: String = randomString("t"),
    year: Int = randomInt(2000, 2099), // 4 digits,
    month: Int = randomInt(1, 12),
    day: Int = randomInt(1, 20)
  ): Ymd {
    return ImmutableYmd(
      type = type,
      year = year,
      month = month,
      day = day
    )
  }
}