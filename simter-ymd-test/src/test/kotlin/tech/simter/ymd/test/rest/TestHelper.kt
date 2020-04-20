package tech.simter.ymd.test.rest

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.test.TestHelper.randomYmd

object TestHelper {
  /** create one ymd */
  fun createOneYmd(
    client: WebTestClient,
    ymd: Ymd = randomYmd()
  ): Ymd {
    client.post()
      .uri("/")
      .contentType(APPLICATION_JSON)
      .bodyValue(ymd)
      .exchange()
      .expectStatus().isNoContent
      .expectBody().isEmpty

    return ymd
  }
}