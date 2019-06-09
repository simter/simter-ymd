package tech.simter.ymd.rest.webflux.handler

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.impl.ImmutableYmd
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import javax.json.Json

/**
 * Test [BatchCreateByTypeHandler]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class BatchCreateByTypeHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val service: YmdService
) {
  private val type = randomString()

  @Test
  fun `Success with data`() {
    // mock
    val list = listOf(
      ImmutableYmd(type = type, year = 2001, month = 1, day = 1),
      ImmutableYmd(type = type, year = 2001, month = 1, day = 2)
    )
    val data = Json.createArrayBuilder()
    list.forEach {
      data.add(
        Json.createObjectBuilder()
          .add("year", it.year)
          .add("month", it.month)
          .add("day", it.day)
      )
    }
    every { service.save(*anyVararg()) } returns Mono.empty()

    // invoke and verify
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .syncBody(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.save(*anyVararg()) }
  }

  @Test
  fun `Success with empty body`() {
    // mock
    every { service.save(any()) } returns Mono.empty()

    // invoke and verify with empty body
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 0) { service.save(any()) }

    // invoke and verify without body
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 0) { service.save(any()) }
  }
}