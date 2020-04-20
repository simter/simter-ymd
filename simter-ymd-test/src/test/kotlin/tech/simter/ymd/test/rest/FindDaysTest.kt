package tech.simter.ymd.test.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.ymd.test.TestHelper.randomType
import tech.simter.ymd.test.TestHelper.randomYmd
import tech.simter.ymd.test.rest.TestHelper.createOneYmd

/**
 * Test find days of type, year and month.
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
class FindDaysTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun found() {
    // prepare data
    val type = randomType()
    val year = 2000
    val month = 1
    val t1y1m1d1 = randomYmd(type = type, year = year, month = month, day = 1)
    val t1y1m1d2 = randomYmd(type = type, year = year, month = month, day = 2)
    val t2y = randomYmd(type = randomType(), year = year, month = month, day = 3)
    createOneYmd(client = client, ymd = t1y1m1d1)
    createOneYmd(client = client, ymd = t1y1m1d2)
    createOneYmd(client = client, ymd = t2y)

    // find it
    client.get().uri("/$type/$year/$month/day")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(2)
      .jsonPath("$[0]").isEqualTo(t1y1m1d2.day)
      .jsonPath("$[1]").isEqualTo(t1y1m1d1.day)
  }

  @Test
  fun notFound() {
    client.get().uri("/${randomType()}/2000/12/day")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}