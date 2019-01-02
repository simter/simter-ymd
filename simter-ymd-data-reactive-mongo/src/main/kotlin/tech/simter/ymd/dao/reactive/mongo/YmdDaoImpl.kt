package tech.simter.ymd.dao.reactive.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.po.Ymd

/**
 * The Reactive MongoDB implementation of [YmdDao].
 *
 * @author RJ
 */
@Component
class YmdDaoImpl @Autowired constructor(
  private val repository: YmdRepository
) : YmdDao {
  override fun save(vararg ymd: Ymd): Mono<Void> {
    return repository.saveAll(ymd.asIterable()).then()
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