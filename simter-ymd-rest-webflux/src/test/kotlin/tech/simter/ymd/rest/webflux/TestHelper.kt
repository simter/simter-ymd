package tech.simter.ymd.rest.webflux

import tech.simter.util.RandomUtils.nextId
import tech.simter.util.RandomUtils.randomInt
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.impl.ImmutableYmd

/**
 * Test utils.
 *
 * @author RJ
 */
object TestHelper {
  /** Generate a random [Ymd] instance */
  fun randomYmd(
    type: String = nextType(),
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

  /** Generate a next [Ymd] type */
  private fun nextType(): String {
    return "type${nextId("type")}"
  }
}