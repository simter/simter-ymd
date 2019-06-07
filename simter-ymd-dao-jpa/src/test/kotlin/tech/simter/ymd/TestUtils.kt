package tech.simter.ymd

import tech.simter.util.RandomUtils
import tech.simter.util.RandomUtils.randomInt
import tech.simter.ymd.po.Ymd

/**
 * Test utils.
 *
 * @author RJ
 */
object TestUtils {
  /** Generate a random [Ymd] instance */
  fun randomYmd(
    id: String = nextId(),
    type: String = nextType(),
    year: Int = randomInt(2000, 2099), // 4 digits,
    month: Int = randomInt(1, 12),
    day: Int = randomInt(1, 20)
  ): Ymd {
    return Ymd(
      id = id,
      type = type,
      year = year,
      month = month,
      day = day
    )
  }

  /** Generate a next [Ymd] type */
  fun nextType(): String {
    return "type${RandomUtils.nextId("type")}"
  }

  /** Generate a next [Ymd] id */
  fun nextId(): String {
    return "id${RandomUtils.nextId("id")}"
  }
}