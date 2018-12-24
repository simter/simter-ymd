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
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.rest.webflux.handler.FindDaysHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.rest.webflux.handler.FindDaysHandlerTest.Cfg
import tech.simter.ymd.service.YmdService
import java.time.MonthDay
import java.time.YearMonth

/**
 * Test [FindDaysHandler]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(FindDaysHandler::class, Cfg::class, UnitTestConfiguration::class)
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

  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(2000, 3000)
    val month = randomInt(1, 12)
    `when`(ymdService.findDays(type, YearMonth.of(year, month))).thenReturn(Flux.empty())

    // invoke and verify
    client.get().uri("/$type/$year/$month/day")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
    verify(ymdService).findDays(type, YearMonth.of(year, month))
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(2000, 3000)
    val month = randomInt(1, 12)
    val days = (2 downTo 1).map { MonthDay.of(month, it) }
    `when`(ymdService.findDays(type, YearMonth.of(year, month))).thenReturn(Flux.just(*days.toTypedArray()))

    // invoke and verify
    client.get().uri("/$type/$year/$month/day")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(days.size)
      .jsonPath("$.[0]").isEqualTo(days[0])
      .jsonPath("$.[1]").isEqualTo(days[1])

    verify(ymdService).findDays(type, YearMonth.of(year, month))
  }
}