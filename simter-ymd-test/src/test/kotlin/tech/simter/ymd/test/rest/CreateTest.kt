package tech.simter.ymd.test.rest

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.ymd.test.TestHelper.randomYmd
import javax.json.Json

/**
 * Test create.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class CreateTest @Autowired constructor(
  private val client: WebTestClient
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

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Success with type+year+month`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
      .add("month", ymd.month)

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Success with type+year`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Failed by empty body`() {
    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue("")
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Empty body!", responseBody)
      }
  }

  @Test
  fun `Failed by without body`() {
    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Empty body!", responseBody)
      }
  }
}