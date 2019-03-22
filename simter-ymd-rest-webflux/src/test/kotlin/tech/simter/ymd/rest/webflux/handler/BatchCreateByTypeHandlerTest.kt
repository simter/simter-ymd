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
import reactor.core.publisher.Mono
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.po.Ymd
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.rest.webflux.handler.BatchCreateByTypeHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.service.YmdService
import javax.json.Json

/**
 * Test [BatchCreateByTypeHandler]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class, BatchCreateByTypeHandler::class, BatchCreateByTypeHandlerTest.Cfg::class)
@MockkBean(YmdService::class)
@WebFluxTest
class BatchCreateByTypeHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val service: YmdService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: BatchCreateByTypeHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  private val type = randomString()

  @Test
  fun `Success with data`() {
    // mock
    val list = listOf(
      Ymd(type = type, year = 2001, month = 1, day = 1),
      Ymd(type = type, year = 2001, month = 1, day = 2)
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
      .contentType(APPLICATION_JSON_UTF8)
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
      .contentType(APPLICATION_JSON_UTF8)
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 0) { service.save(any()) }

    // invoke and verify without body
    client.post().uri("/$type")
      .contentType(APPLICATION_JSON_UTF8)
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(exactly = 0) { service.save(any()) }
  }
}