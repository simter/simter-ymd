package tech.simter.ymd.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.dto.YearNode
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.time.YearMonth

/**
 * The Service implementation of [YmdService].
 *
 * @author JF
 */
@Component
@Transactional
class YmdServiceImpl @Autowired constructor(
  private val dao: YmdDao
) : YmdService {
  override fun findYears(type: String): Flux<Year> {
    return dao.findYears(type)
  }

  override fun findMonths(type: String, year: Year): Flux<Month> {
    return dao.findMonths(type, year)
  }

  override fun findDays(type: String, yearMonth: YearMonth): Flux<MonthDay> {
    return dao.findDays(type, yearMonth)
  }

  override fun findYearsWithLatestDay(type: String): Flux<YearNode> {
    return dao.findYearsWithLatestDay(type)
  }
}