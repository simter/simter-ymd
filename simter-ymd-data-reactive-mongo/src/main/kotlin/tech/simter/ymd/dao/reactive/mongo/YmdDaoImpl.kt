package tech.simter.ymd.dao.reactive.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.dto.YearNode
import tech.simter.ymd.po.Ymd
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.time.YearMonth

/**
 * The Reactive MongoDB implementation of [YmdDao].
 *
 * @author RJ
 */
@Component
class YmdDaoImpl @Autowired constructor(
  private val repository: YmdReactiveRepository
) : YmdDao {
  override fun save(vararg ymd: Ymd): Mono<Void> {
    TODO("not implemented")
  }

  override fun findYears(type: String): Flux<Year> {
    TODO("not implemented")
  }

  override fun findMonths(type: String, year: Year): Flux<Month> {
    TODO("not implemented")
  }

  override fun findDays(type: String, yearMonth: YearMonth): Flux<MonthDay> {
    TODO("not implemented")
  }

  override fun findYearsWithLatestDay(type: String): Flux<YearNode> {
    TODO("not implemented")
  }
}