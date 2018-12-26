package tech.simter.ymd

import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.po.Ymd

/**
 * Test utils.
 *
 * @author RJ
 */
object TestUtils {
  /** Generate a random [Ymd] instance */
  fun randomYmd(
    type: String = randomString("t"),
    year: Int = randomInt(2000, 2099), // 4 digits,
    month: Int = randomInt(1, 12),
    day: Int = randomInt(1, 20)
  ): Ymd {
    return Ymd(
      type = type,
      year = year,
      month = month,
      day = day
    )
  }
}