package tech.simter.ymd.rest.webflux.handler

import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.dto.MonthToDayNode
import tech.simter.ymd.dto.YearToMonthDayNode
import tech.simter.ymd.dto.YearToMonthNode
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.rest.webflux.handler.FindYearsHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.rest.webflux.handler.FindYearsHandlerTest.Cfg
import tech.simter.ymd.service.YmdService
import java.time.Year

/**
 * Test [FindYearsHandler]
 *
 * @author RJ
 */
@SpringJUnitConfig(FindYearsHandler::class, Cfg::class, UnitTestConfiguration::class)
@MockBean(YmdService::class)
@WebFluxTest
class FindYearsHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val ymdService: YmdService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: FindYearsHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    `when`(ymdService.findYears(type)).thenReturn(Flux.empty())

    // invoke
    client.get().uri("/$type/year")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
    verify(ymdService).findYears(type)
  }

  @Test
  fun `Found years without query param`() {
    // mock
    val type = randomString()
    val years = (2 downTo 1).map { Year.of(2000 + it) }
    `when`(ymdService.findYears(type)).thenReturn(Flux.just(*years.toTypedArray()))

    // invoke
    client.get().uri("/$type/year")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(years.size)
      .jsonPath("$.[0]").isEqualTo(years[0].value)
      .jsonPath("$.[1]").isEqualTo(years[1].value)
    verify(ymdService).findYears(type)
    verify(ymdService, times(0)).findYearsWithLatestMonthDays(type)
    verify(ymdService, times(0)).findYearsWithLatestYearMonths(type)
  }

  @Test
  fun `Found years with latest year months`() {
    // mock
    val type = randomString()
    val latestYearMonths = listOf(2, 1)
    val years = listOf(
      YearToMonthNode(year = 2009, months = latestYearMonths),
      YearToMonthNode(year = 2008)
    )
    `when`(ymdService.findYearsWithLatestYearMonths(type)).thenReturn(Flux.just(*years.toTypedArray()))

    // invoke
    client.get().uri("/$type/year?with-latest-year-months")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(years.size)
      .jsonPath("$.[0].year").isEqualTo(years[0].year)
      .jsonPath("$.[0].months.length()").isEqualTo(years[0].months!!.size)
      .jsonPath("$.[0].months.[0]").isEqualTo(years[0].months!![0])
      .jsonPath("$.[0].months.[1]").isEqualTo(years[0].months!![1])
      .jsonPath("$.[1].year").isEqualTo(years[1].year)
      .jsonPath("$.[1].months").doesNotExist()
    verify(ymdService, times(0)).findYears(type)
    verify(ymdService, times(0)).findYearsWithLatestMonthDays(type)
    verify(ymdService).findYearsWithLatestYearMonths(type)
  }

  @Test
  fun `Found years with latest month days`() {
    // mock
    val type = randomString()
    val latestYearMonths = listOf(
      MonthToDayNode(month = 2, days = listOf(9, 8)),
      MonthToDayNode(month = 1)
    )
    val years = listOf(
      YearToMonthDayNode(year = 2009, months = latestYearMonths),
      YearToMonthDayNode(year = 2008)
    )
    `when`(ymdService.findYearsWithLatestMonthDays(type)).thenReturn(Flux.just(*years.toTypedArray()))

    // invoke
    client.get().uri("/$type/year?with-latest-month-days")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(years.size)
      .jsonPath("$.[0].year").isEqualTo(years[0].year)
      .jsonPath("$.[0].months.length()").isEqualTo(years[0].months!!.size)
      .jsonPath("$.[0].months.[0].month").isEqualTo(years[0].months!![0].month)
      .jsonPath("$.[0].months.[0].days.length()").isEqualTo(years[0].months!![0].days!!.size)
      .jsonPath("$.[0].months.[0].days[0]").isEqualTo(years[0].months!![0].days!![0])
      .jsonPath("$.[0].months.[0].days[1]").isEqualTo(years[0].months!![0].days!![1])
      .jsonPath("$.[0].months.[1].month").isEqualTo(years[0].months!![1].month)
      .jsonPath("$.[0].months.[1].days").doesNotExist()
      .jsonPath("$.[1].year").isEqualTo(years[1].year)
      .jsonPath("$.[1].months").doesNotExist()
    verify(ymdService, times(0)).findYears(type)
    verify(ymdService).findYearsWithLatestMonthDays(type)
    verify(ymdService, times(0)).findYearsWithLatestYearMonths(type)
  }
}