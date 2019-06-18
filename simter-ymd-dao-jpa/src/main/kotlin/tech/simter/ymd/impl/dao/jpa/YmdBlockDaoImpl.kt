package tech.simter.ymd.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.impl.dao.jpa.po.YmdPo

/**
 * The JPA implementation of [YmdBlockDao].
 *
 * @author RJ
 */
@Repository
class YmdBlockDaoImpl @Autowired constructor(
  private val repository: YmdJpaRepository
) : YmdBlockDao {
  override fun save(vararg ymd: Ymd) {
    repository.saveAll(ymd.map { YmdPo.from(it) })
  }

  override fun findYears(type: String): List<Int> {
    return repository.findYears(type)
  }

  override fun findMonths(type: String, year: Int): List<Int> {
    return repository.findMonths(type, year)
  }

  override fun findDays(type: String, year: Int, month: Int): List<Int> {
    return repository.findDays(type, year, month)
  }
}