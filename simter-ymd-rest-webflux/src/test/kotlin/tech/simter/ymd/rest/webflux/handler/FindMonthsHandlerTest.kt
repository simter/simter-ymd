package tech.simter.ymd.rest.webflux.handler

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
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
import tech.simter.ymd.rest.webflux.handler.FindMonthsHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.rest.webflux.handler.FindMonthsHandlerTest.Cfg
import tech.simter.ymd.service.YmdService

/**
 * Test [FindMonthsHandler]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(FindMonthsHandler::class, Cfg::class, UnitTestConfiguration::class)
@MockkBean(YmdService::class)
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

  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(2000, 3000)
    every { ymdService.findMonths(type, year) } returns Flux.empty()

    // invoke and verify
    client.get().uri("/$type/$year/month")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
    verify(exactly = 1) { ymdService.findMonths(type, year) }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(2000, 3000)
    val months = (2 downTo 1).map { it }
    every { ymdService.findMonths(type, year) } returns Flux.just(*months.toTypedArray())

    // invoke and verify
    client.get().uri("/$type/$year/month")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(months.size)
      .jsonPath("$.[0]").isEqualTo(months[0])
      .jsonPath("$.[1]").isEqualTo(months[1])

    verify(exactly = 1) { ymdService.findMonths(type, year) }
  }
}