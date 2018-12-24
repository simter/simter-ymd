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
import tech.simter.ymd.rest.webflux.handler.FindMonthsHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.service.YmdService
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter

/**
 * Test [FindMonthsHandler]
 *
 * @author XA
 */
@SpringJUnitConfig(FindMonthsHandler::class, FindMonthsHandlerTest.Cfg::class, UnitTestConfiguration::class)
@MockBean(YmdService::class)
@WebFluxTest
class FindMonthsHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val ymdService: YmdService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: FindMonthsHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  private val year = Year.of(2018)
  private val y = year.format(DateTimeFormatter.ofPattern("yyyy"))
  private val type = randomString("type")
  private val url = "/$type/month?y=$y"

  @Test
  fun `Found nothing`() {
    // mock
    `when`(ymdService.findMonths(type, year))
      .thenReturn(Flux.empty())

    // invoke and verify
    client.get().uri(url)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty

    verify(ymdService).findMonths(type, year)
  }

  @Test
  fun `Found something`() {
    // mock
    val month = (1..2).map { Month.of(1 + it) }
    `when`(ymdService.findMonths(type, year))
      .thenReturn(Flux.just(*month.toTypedArray()))

    // invoke and verify
    client.get().uri(url)
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(month.size)
      .jsonPath("$.[0]").isEqualTo(month[0].value)
      .jsonPath("$.[1]").isEqualTo(month[1].value)

    verify(ymdService).findMonths(type, year)
  }
}