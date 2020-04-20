package tech.simter.ymd.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.ymd.impl.ImmutableYmd
import tech.simter.ymd.test.TestHelper.randomType
import javax.json.Json

/**
 * Test batch create.
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@WebFluxTest
class BatchCreateByTypeTest @Autowired constructor(
  private val client: WebTestClient
) {
  private val type = randomType()

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

    // invoke and verify
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .bodyValue(data.build().toString())
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Success with empty body`() {
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .bodyValue("")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Success without body`() {
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}