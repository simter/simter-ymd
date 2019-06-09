package tech.simter.ymd.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.OPERATION_READ
import tech.simter.ymd.OPERATION_SAVE
import tech.simter.ymd.PACKAGE
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.core.MonthWithItsDays
import tech.simter.ymd.core.YearWithItsMonthDays
import tech.simter.ymd.core.YearWithItsMonths
import tech.simter.ymd.impl.ImmutableMonthWithItsDays
import tech.simter.ymd.impl.ImmutableYearWithItsMonthDays
import tech.simter.ymd.impl.ImmutableYearWithItsMonths

/**
 * The Service implementation of [YmdService].
 *
 * @author RJ
 */
@Service
class YmdServiceImpl @Autowired constructor(
  @Qualifier("$PACKAGE.ModuleAuthorizer")
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao
) : YmdService {
  override fun save(vararg ymd: Ymd): Mono<Void> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_SAVE).then(
      dao.save(*ymd)
    )
  }

  override fun findYears(type: String): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findYears(type)
    )
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findMonths(type, year)
    )
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return moduleAuthorizer.verifyHasPermission(OPERATION_READ).thenMany(
      dao.findDays(type, year, month)
    )
  }

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
              .map { ImmutableYearWithItsMonths(year = latestYear, months = if (it.isEmpty()) null else it) }

            // concat with the rest of years
            latestYearWithMonths.map {
              listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> ImmutableYearWithItsMonths(year = y) })
            }
          }
        }.flatMapIterable { it }
    )
  }

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
            .map { ImmutableMonthWithItsDays(month = latestMonth, days = if (it.isEmpty()) null else it) }

          // concat with the rest of months
          latestMonthWithDays.map {
            listOf(it).plus(months.filterIndexed { index, _ -> index > 0 }.map { m -> ImmutableMonthWithItsDays(month = m) })
          }
        }
      }.flatMapIterable { it }
  }

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
                @Suppress("UNCHECKED_CAST")
                ImmutableYearWithItsMonthDays(
                  year = latestYear,
                  months = if (it.isEmpty()) null else it as List<ImmutableMonthWithItsDays>
                ) as YearWithItsMonthDays
              }

            // concat with the rest of years
            latestYearWithMonths.map {
              listOf(it).plus(years.filterIndexed { index, _ -> index > 0 }.map { y -> ImmutableYearWithItsMonthDays(year = y) })
            }
          }
        }.flatMapIterable { it }
    )
  }
}