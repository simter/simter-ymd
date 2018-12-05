package tech.simter.ymd.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.dto.YearNode
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.time.YearMonth

/**
 * The JPA implementation of [YmdDao].
 *
 * @author RJ
 */
@Component
class YmdDaoImpl @Autowired constructor(
  private val repository: YmdJpaRepository
) : YmdDao {
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