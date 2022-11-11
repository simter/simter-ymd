package tech.simter.ymd.rest.webflux.handler

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.test.TestHelper.randomYmd
import javax.json.Json

/**
 * Test [CreateHandler]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class CreateHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val service: YmdService
) {
  @Test
  fun `Success with type+year+month+day`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
      .add("month", ymd.month)
      .add("day", ymd.day)
    every { service.create(any()) } returns Mono.empty()

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(any()) }
  }

  @Test
  fun `Success with type+year+month`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
      .add("month", ymd.month)
    every { service.create(any()) } returns Mono.empty()

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(any()) }
  }

  @Test
  fun `Success with type+year`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
    every { service.create(any()) } returns Mono.empty()

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 1) { service.create(any()) }
  }

  @Test
  fun `Failed by empty body`() {
    // mock
    every { service.create(any()) } returns Mono.empty()

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Empty body!", responseBody)
      }
    verify(exactly = 0) { service.create(any()) }
  }
}