package tech.simter.ymd.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.po.Ymd

/**
 * The JPA implementation of [YmdDao].
 *
 * @author RJ
 */
@Component
class YmdDaoImpl @Autowired constructor(
  private val repository: YmdJpaRepository
) : YmdDao {
  override fun save(vararg ymd: Ymd): Mono<Void> {
    return try {
      repository.saveAll(ymd.asIterable())
      Mono.empty()
    } catch (e: Exception) {
      Mono.error(e)
    }
  }

  override fun findYears(type: String): Flux<Int> {
    return Flux.fromIterable(repository.findYears(type))
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return Flux.fromIterable(repository.findMonths(type, year))
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return Flux.fromIterable(repository.findDays(type, year, month))
  }
}