package tech.simter.ymd.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.reactive.jpa.ReactiveJpaWrapper
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.po.YmdPo

/**
 * The JPA implementation of [YmdDao].
 *
 * @author RJ
 */
@Repository
class YmdDaoImpl @Autowired constructor(
  private val blockDao: YmdBlockDao,
  private val wrapper: ReactiveJpaWrapper
) : YmdDao {
  override fun create(vararg ymd: Ymd): Mono<Void> {
    return wrapper.fromRunnable { blockDao.save(*ymd.map { YmdPo.from(it) }.toTypedArray()) }
  }

  override fun findYears(type: String): Flux<Int> {
    return wrapper.fromIterable { blockDao.findYears(type) }
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return wrapper.fromIterable { blockDao.findMonths(type, year) }
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return wrapper.fromIterable { blockDao.findDays(type, year, month) }
  }
}