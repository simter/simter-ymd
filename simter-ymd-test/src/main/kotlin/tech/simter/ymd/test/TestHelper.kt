package tech.simter.ymd.test

import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.core.Ymd
import java.time.Month

/**
 * Some common tools for unit test.
 *
 * @author RJ
 */
object TestHelper {
  /** Create a random type */
  fun randomType(): String {
    return randomString(len = 6)
  }

  /** Create a random year */
  fun randomYear(): Int {
    return randomInt(from = 2000, until = 2999)
  }

  /** Create a random month */
  fun randomMonth(): Int {
    return randomInt(from = 1, until = 12)
  }

  /** Create a random year */
  fun randomDay(month: Int = randomMonth()): Int {
    return randomInt(from = 1, until = Month.of(month).maxLength())
  }

  /** Create a random [Ymd] instance */
  fun randomYmd(
    type: String = randomType(),
    year: Int = randomYear(),
    month: Int = randomMonth(),
    day: Int = randomDay()
  ): Ymd {
    val maxDay = Month.of(month).maxLength()
    return Ymd(
      type = type,
      year = year,
      month = month,
      day = if (day > maxDay) maxDay else day
    )
  }
}