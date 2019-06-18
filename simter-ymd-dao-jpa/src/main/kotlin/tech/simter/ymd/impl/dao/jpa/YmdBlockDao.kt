package tech.simter.ymd.impl.dao.jpa

import tech.simter.ymd.core.Ymd

/**
 * The [Ymd] block Dao Interface.
 *
 * @author RJ
 */
interface YmdBlockDao {
  /**
   * Save or update all the given [ymd] entity.
   */
  fun save(vararg ymd: Ymd)

  /**
   * Find all years of the specific [type].
   *
   * The result is ordered by year desc.
   */
  fun findYears(type: String): List<Int>

  /**
   * Find all months of the specific [type] and [year].
   *
   * The result is ordered by month desc.
   */
  fun findMonths(type: String, year: Int): List<Int>

  /**
   * Find all days of the specific [type], [year] and [month].
   *
   * The result is ordered by day desc.
   */
  fun findDays(type: String, year: Int, month: Int): List<Int>
}