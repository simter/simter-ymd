package tech.simter.ymd.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.AUTHORIZER_KEY
import tech.simter.ymd.core.*
import tech.simter.ymd.core.Ymd.Companion.OPERATION_EDIT
import tech.simter.ymd.core.Ymd.Companion.OPERATION_READ

/**
 * The Service implementation of [YmdService].
 *
 * @author RJ
 */
@Service
class YmdServiceImpl @Autowired constructor(
  @Qualifier("$AUTHORIZER_KEY.authorizer")
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao
) : YmdService {
  @Transactional(readOnly = false)
  override fun create(vararg ymd: Ymd): Mono<Void> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_EDIT).then(
      dao.create(*ymd)
    )
  }

  @Transactional(readOnly = true)
  override fun findYears(type: String): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findYears(type)
    )
  }

  @Transactional(readOnly = true)
  override fun findMonths(type: String, year: Int): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findMonths(type, year)
    )
  }

  @Transactional(readOnly = true)
  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findDays(type, year, month)
    )
  }

  @Transactional(readOnly = true)
  override fun findYearsWithLatestYearMonths(type: String): Flux<YearWithItsMonths> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findYears(type)
        .collectList()
        .flatMap { years ->
          if (years.isEmpty()) Mono.empty()
          else {
            // find latest year's months
            val latestYear = years[0]
            val latestYearWithMonths: Mono<YearWithItsMonths> = dao.findMonths(type = type, year = latestYear)
              .collectList()
              .map { YearWithItsMonths(year = latestYear, months = if (it.isEmpty()) null else it) }

            // concat with the rest of years
            latestYearWithMonths.map {
              listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> YearWithItsMonths(year = y) })
            }
          }
        }.flatMapIterable { it }
    )
  }

  @Transactional(readOnly = true)
  override fun findMonthsWithLatestMonthDays(type: String, year: Int): Flux<MonthWithItsDays> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      findMonthsWithLatestMonthDays2(type, year)
    )
  }

  private fun findMonthsWithLatestMonthDays2(type: String, year: Int): Flux<MonthWithItsDays> {
    return dao.findMonths(type, year)
      .collectList()
      .flatMap { months ->
        if (months.isEmpty()) Mono.empty()
        else {
          // find latest month's days
          val latestMonth = months[0]
          val latestMonthWithDays = dao.findDays(type = type, year = year, month = latestMonth)
            .collectList()
            .map { MonthWithItsDays(month = latestMonth, days = if (it.isEmpty()) null else it) }

          // concat with the rest of months
          latestMonthWithDays.map {
            listOf(it).plus(months.filterIndexed { index, _ -> index > 0 }.map { m -> MonthWithItsDays(month = m) })
          }
        }
      }.flatMapIterable { it }
  }

  @Transactional(readOnly = true)
  override fun findYearsWithLatestMonthDays(type: String): Flux<YearWithItsMonthDays> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findYears(type)
        .collectList()
        .flatMap { years ->
          if (years.isEmpty()) Mono.empty()
          else {
            // find latest year's months with latest month's days
            val latestYear = years[0]
            val latestYearWithMonths = findMonthsWithLatestMonthDays2(type = type, year = latestYear)
              .collectList()
              .map {
                YearWithItsMonthDays(
                  year = latestYear,
                  months = if (it.isEmpty()) null else it as List<MonthWithItsDays>
                )
              }

            // concat with the rest of years
            latestYearWithMonths.map {
              listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> YearWithItsMonthDays(year = y) })
            }
          }
        }.flatMapIterable { it }
    )
  }
}