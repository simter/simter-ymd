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
 * Test find years of type.
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
class FindYearsTest @Autowired constructor(
  private val client: WebTestClient
) {
  @Test
  fun `Found nothing`() {
    client.get().uri("/${randomType()}/year")
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty
  }

  @Test
  fun `Found years without query param`() {
    // prepare data
    val type = randomType()
    val t1y1 = randomYmd(type = type, year = 2001)
    val t1y2 = randomYmd(type = type, year = 2002, month = 1)
    val t1y2m2 = randomYmd(type = type, year = 2002, month = 2)
    val t2y = randomYmd(type = randomType(), year = 2003)
    createOneYmd(client = client, ymd = t1y1)
    createOneYmd(client = client, ymd = t1y2)
    createOneYmd(client = client, ymd = t1y2m2)
    createOneYmd(client = client, ymd = t2y)

    // find it
    client.get().uri("/$type/year")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.size()").isEqualTo(2)

      .jsonPath("$[0]").isEqualTo(t1y2.year)
      .jsonPath("$[1]").isEqualTo(t1y1.year)
  }

  @Test
  fun `Found years with latest year months`() {
    // prepare data
    val type = randomType()
    val t1y1 = randomYmd(type = type, year = 2001)
    val t1y2m1 = randomYmd(type = type, year = 2002, month = 1)
    val t1y2m2 = randomYmd(type = type, year = 2002, month = 2, day = 1)
    val t1y2m2d2 = randomYmd(type = type, year = 2002, month = 2, day = 2)
    val t2y = randomYmd(type = randomType(), year = 2003)
    createOneYmd(client = client, ymd = t1y1)
    createOneYmd(client = client, ymd = t1y2m1)
    createOneYmd(client = client, ymd = t1y2m2)
    createOneYmd(client = client, ymd = t1y2m2d2)
    createOneYmd(client = client, ymd = t2y)

    // find it
    client.get().uri("/$type/year?with-latest-year-months")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(2)
      .jsonPath("$[0].year").isEqualTo(t1y2m1.year)
      .jsonPath("$[0].months.length()").isEqualTo(2)
      .jsonPath("$[0].months[0]").isEqualTo(t1y2m2.month)
      .jsonPath("$[0].months[1]").isEqualTo(t1y2m1.month)
      .jsonPath("$[1].year").isEqualTo(t1y1.year)
      .jsonPath("$[1].months").doesNotExist()
  }

  @Test
  fun `Found years with latest month days`() {
    // prepare data
    val type = randomType()
    val t1y1 = randomYmd(type = type, year = 2001)
    val t1y2m1 = randomYmd(type = type, year = 2002, month = 1)
    val t1y2m2d1 = randomYmd(type = type, year = 2002, month = 2, day = 1)
    val t1y2m2d2 = randomYmd(type = type, year = 2002, month = 2, day = 2)
    val t2y = randomYmd(type = randomType(), year = 2003)
    createOneYmd(client = client, ymd = t1y1)
    createOneYmd(client = client, ymd = t1y2m1)
    createOneYmd(client = client, ymd = t1y2m2d1)
    createOneYmd(client = client, ymd = t1y2m2d2)
    createOneYmd(client = client, ymd = t2y)

    // find it
    client.get().uri("/$type/year?with-latest-month-days")
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.length()").isEqualTo(2)
      .jsonPath("$[0].year").isEqualTo(t1y2m1.year)
      .jsonPath("$[0].months.length()").isEqualTo(2)
      .jsonPath("$[0].months[0].month").isEqualTo(t1y2m2d1.month)
      .jsonPath("$[0].months[0].days.length()").isEqualTo(2)
      .jsonPath("$[0].months[0].days[0]").isEqualTo(t1y2m2d2.day)
      .jsonPath("$[0].months[0].days[1]").isEqualTo(t1y2m2d1.day)
      .jsonPath("$[0].months[1].month").isEqualTo(t1y2m1.month)
      .jsonPath("$[0].months[1].days").doesNotExist()
      .jsonPath("$[1].year").isEqualTo(t1y1.year)
      .jsonPath("$[1].months").doesNotExist()
  }
}