/**
 * 这个包只能用于单元测试，请不要在正式代码中引用。
 *
 * @author RJ
 */
package tech.simter.ymd.dao.jpa

import tech.simter.ymd.po.Ymd
import java.util.*

/**
 * 单元测试辅助工具类。
 *
 * @author RJ
 */
object TestUtils {
  private var strMap = hashMapOf("" to 0)
  /** 生成带固定前缀的唯一字符串 */
  fun randomString(prefix: String = ""): String {
    return if (prefix == "") UUID.randomUUID().toString()
    else {
      if (!strMap.containsKey(prefix)) strMap[prefix] = 1
      else strMap[prefix] = strMap[prefix]!! + 1
      "$prefix${strMap[prefix]}"
    }
  }

  /** 在指定区间随机生成一个数字 */
  fun randomInt(start: Int = 0, end: Int = 100) = Random().nextInt(end + 1 - start) + start

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