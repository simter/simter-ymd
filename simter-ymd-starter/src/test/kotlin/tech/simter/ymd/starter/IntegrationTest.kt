package tech.simter.ymd.starter

import org.junit.jupiter.api.Disabled
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * Run test in the real server.
 * @author RJ
 */
@Disabled
class IntegrationTest {
  private val webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8086").build()
  private val contextPath = ""
}