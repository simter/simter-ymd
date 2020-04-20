package tech.simter.ymd.impl.dao.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.mongo.po.YmdPo

/**
 * The Reactive MongoDB implementation of [YmdDao].
 *
 * @author RJ
 */
@Repository
class YmdDaoImpl @Autowired constructor(
  private val repository: YmdRepository
) : YmdDao {
  override fun create(vararg ymd: Ymd): Mono<Void> {
    return repository.saveAll(ymd.map { YmdPo.from(it) }).then()
  }

  override fun findYears(type: String): Flux<Int> {
    return repository.findYears(type)
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return repository.findMonths(type, year)
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return repository.findDays(type, year, month)
  }
}