package tech.simter.ymd.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.dto.MonthToDayNode
import tech.simter.ymd.dto.YearToMonthDayNode
import tech.simter.ymd.dto.YearToMonthNode
import tech.simter.ymd.po.Ymd
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
  override fun save(vararg ymd: Ymd): Mono<Void> {
    return dao.save(*ymd)
  }

  override fun findYears(type: String): Flux<Year> {
    return dao.findYears(type)
  }

  override fun findMonths(type: String, year: Year): Flux<Month> {
    return dao.findMonths(type, year)
  }

  override fun findDays(type: String, yearMonth: YearMonth): Flux<MonthDay> {
    return dao.findDays(type, yearMonth)
  }

  override fun findYearsWithLatestYearMonths(type: String): Flux<YearToMonthNode> {
    return dao.findYears(type)
      .collectList()
      .flatMap { years ->
        if (years.isEmpty()) Mono.empty()
        else {
          // find latest year's months
          val latestYear = years[0]
          val latestYearWithMonths: Mono<YearToMonthNode> = dao.findMonths(type = type, year = latestYear)
            .map { it.value }
            .collectList()
            .map { YearToMonthNode(year = latestYear.value, months = if (it.isEmpty()) null else it) }

          // concat with the rest of years
          latestYearWithMonths.map {
            listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> YearToMonthNode(year = y.value) })
          }
        }
      }.flatMapIterable { it }
  }

  override fun findMonthsWithLatestMonthDays(type: String, year: Year): Flux<MonthToDayNode> {
    return dao.findMonths(type, year)
      .collectList()
      .flatMap { months ->
        if (months.isEmpty()) Mono.empty()
        else {
          // find latest month's days
          val latestMonth = months[0]
          val latestMonthWithDays = dao.findDays(type = type, yearMonth = YearMonth.of(year.value, latestMonth.value))
            .map { it.dayOfMonth }
            .collectList()
            .map { MonthToDayNode(month = latestMonth.value, days = if (it.isEmpty()) null else it) }

          // concat with the rest of months
          latestMonthWithDays.map {
            listOf(it).plus(months.filterIndexed { index, _ -> index > 0 }.map { m -> MonthToDayNode(month = m.value) })
          }
        }
      }.flatMapIterable { it }
  }

  override fun findYearsWithLatestMonthDays(type: String): Flux<YearToMonthDayNode> {
    return dao.findYears(type)
      .collectList()
      .flatMap { years ->
        if (years.isEmpty()) Mono.empty()
        else {
          // find latest year's months with latest month's days
          val latestYear = years[0]
          val latestYearWithMonths = findMonthsWithLatestMonthDays(type = type, year = latestYear)
            .collectList()
            .map { YearToMonthDayNode(year = latestYear.value, months = if (it.isEmpty()) null else it) }

          // concat with the rest of years
          latestYearWithMonths.map {
            listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> YearToMonthDayNode(year = y.value) })
          }
        }
      }.flatMapIterable { it }
  }
}