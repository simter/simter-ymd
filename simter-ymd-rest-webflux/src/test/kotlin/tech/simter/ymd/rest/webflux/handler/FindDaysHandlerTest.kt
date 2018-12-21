package tech.simter.ymd.rest.webflux.handler

import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
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
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.rest.webflux.handler.FindDaysHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.service.YmdService
import java.time.MonthDay
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * Test [FindDaysHandler]
 *
 * @author XA
 */
@SpringJUnitConfig(FindDaysHandler::class, FindDaysHandlerTest.Cfg::class, UnitTestConfiguration::class)
@MockBean(YmdService::class)
@WebFluxTest
class FindDaysHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val ymdService: YmdService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: FindDaysHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  private val type = randomString("type")
  private val yearMonth = YearMonth.of(2018, 1)
  private val ym = yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"))
  private val url = "/$type/day?ym=$ym"

  @Test
  fun `Found nothing`() {
    // mock
    `when`(ymdService.findDays(type, yearMonth))
      .thenReturn(Flux.empty())

    // invoke
    client.get()
      .uri(url)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty

    verify(ymdService).findDays(type, yearMonth)
  }

  @Test
  fun `Found something`() {
    // mock
    val monthDay = (2 downTo 1).map { MonthDay.of(1,5+it) }
    `when`(ymdService.findDays(type, yearMonth))
      .thenReturn(Flux.just(*monthDay.toTypedArray()))

    // invoke and verify
    client.get().uri(url)
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(monthDay.size)
      .jsonPath("$.[0]").isEqualTo(monthDay[0])
      .jsonPath("$.[1]").isEqualTo(monthDay[1])

    verify(ymdService).findDays(type, yearMonth)
  }
}