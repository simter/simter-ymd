package tech.simter.ymd

import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.po.Ymd

/**
 * 单元测试辅助工具类。
 *
 * @author RJ
 */
object TestUtils {
  /** 随机生成一个刷卡记录 */
  fun randomYmd(
    type: String = randomString("t"),
    year: Int = randomInt(2000, 2099), // 4 位数字,
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