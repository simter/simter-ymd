package tech.simter.ymd.rest.webflux.handler

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.rest.webflux.UnitTestConfiguration

/**
 * Test [FindMonthsHandler]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class FindMonthsHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val ymdService: YmdService
) {
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
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(months.size)
      .jsonPath("$.[0]").isEqualTo(months[0])
      .jsonPath("$.[1]").isEqualTo(months[1])

    verify(exactly = 1) { ymdService.findMonths(type, year) }
  }
}