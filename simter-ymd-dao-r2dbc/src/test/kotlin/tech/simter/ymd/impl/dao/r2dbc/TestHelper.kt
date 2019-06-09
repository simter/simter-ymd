package tech.simter.ymd.impl.dao.r2dbc

import tech.simter.util.RandomUtils.nextId
import tech.simter.util.RandomUtils.randomInt
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.impl.dao.r2dbc.po.YmdPo

/**
 * Provide public method of test
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
  ): YmdPo {
    return YmdPo(
      type = type,
      year = year,
      month = month,
      day = day
    )
  }

  /** Generate a next [Ymd] type */
  fun nextType(): String {
    return "type${nextId("type")}"
  }

  /** Generate a next [Ymd] id */
  fun nextId(): String {
    return "id${nextId("id")}"
  }
}