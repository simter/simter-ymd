package tech.simter.ymd.rest.webflux.handler

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.rest.webflux.UnitTestConfiguration
import tech.simter.ymd.rest.webflux.handler.CreateHandler.Companion.REQUEST_PREDICATE
import tech.simter.ymd.service.YmdService
import javax.json.Json

/**
 * Test [CreateHandler]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class, CreateHandler::class, CreateHandlerTest.Cfg::class)
@MockBean(YmdService::class)
@WebFluxTest
class CreateHandlerTest @Autowired constructor(
  private val client: WebTestClient,
  private val service: YmdService
) {
  @Configuration
  class Cfg {
    @Bean
    fun theRoute(handler: CreateHandler): RouterFunction<ServerResponse> = route(REQUEST_PREDICATE, handler)
  }

  @Test
  fun `Success with type+year+month+day`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
      .add("month", ymd.month)
      .add("day", ymd.day)
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(service).save(any())
  }

  @Test
  fun `Success with type+year+month`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
      .add("month", ymd.month)
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(service).save(any())
  }

  @Test
  fun `Success with type+year`() {
    // mock
    val ymd = randomYmd()
    val data = Json.createObjectBuilder()
      .add("type", ymd.type)
      .add("year", ymd.year)
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(data.build().toString())
      .exchange()
      .expectStatus().isNoContent.expectBody().isEmpty
    verify(service).save(any())
  }

  @Test
  fun `Failed by missing type`() {
    // mock
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody("""{"year": 2019, "month": 1}""")
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Missing type value!", responseBody)
      }
    verify(service, times(0)).save(any())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody("{}") // empty json
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Missing type value!", responseBody)
      }
    verify(service, times(0)).save(any())
  }

  @Test
  fun `Failed by missing year`() {
    // mock
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody("""{"type": "test", "month": 1}""")
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Missing year value!", responseBody)
      }
    verify(service, times(0)).save(any())
  }

  @Test
  fun `Failed by empty body`() {
    // mock
    `when`(service.save(any())).thenReturn(Mono.empty())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody("")
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Missing type value!", responseBody)
      }
    verify(service, times(0)).save(any())

    // invoke and verify
    client.post().uri("/")
      .contentType(APPLICATION_JSON_UTF8)
      .exchange()
      .expectStatus().isBadRequest
      .expectHeader().contentType("${MediaType.TEXT_PLAIN_VALUE};charset=UTF-8")
      .expectBody(String::class.java).returnResult().apply {
        assertEquals("Missing type value!", responseBody)
      }
    verify(service, times(0)).save(any())
  }
}