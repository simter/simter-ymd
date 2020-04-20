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
 * Test find months of type and year.
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
class FindMonthsTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun found() {
    // prepare data
    val type = randomType()
    val year = 2000
    val t1y1m1 = randomYmd(type = type, year = year, month = 1)
    val t1y1m2 = randomYmd(type = type, year = year, month = 2, day = 1)
    val t1y1m2d2 = randomYmd(type = type, year = year, month = 2, day = 2)
    val t2y = randomYmd(type = randomType(), year = year, month = 3)
    createOneYmd(client = client, ymd = t1y1m1)
    createOneYmd(client = client, ymd = t1y1m2)
    createOneYmd(client = client, ymd = t1y1m2d2)
    createOneYmd(client = client, ymd = t2y)

    // find it
    client.get().uri("/$type/$year/month")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(2)
      .jsonPath("$[0]").isEqualTo(t1y1m2.month)
      .jsonPath("$[1]").isEqualTo(t1y1m1.month)
  }

  @Test
  fun notFound() {
    client.get().uri("/${randomType()}/2000/month")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }
}