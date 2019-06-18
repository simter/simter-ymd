package tech.simter.ymd.impl.dao.jpa

import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.impl.dao.jpa.po.YmdPo

/**
 * Provide public method for test.
 *
 * @author RJ
 */
object TestHelper {
  /** Generate a random [Ymd] instance */
  fun randomYmd(
    type: String = randomType(),
    year: Int = randomInt(2000, 2099), // 4 digits,
    month: Int = randomInt(1, 12),
    day: Int = randomInt(1, 20)
  ): YmdPo {
    return YmdPo(
      type = type,
      year = year,
      month = month,
      day = day
    )
  }

  /** Generate a next [Ymd] type */
  fun randomType(): String {
    val s = randomString()
    return s.substring(0, 5) + s.substring(s.length - 5)
  }
}