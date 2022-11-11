package tech.simter.ymd.rest.webflux.handler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.ymd.core.YmdService

/**
 * Find all months of the specific type and year [FindMonthsHandler]
 *
 * @author RJ
 * @author XA
 */
@Component("tech.simter.ymd.rest.webflux.handler.FindMonthsHandler")
class FindMonthsHandler @Autowired constructor(
  private val json: Json,
  private val ymdService: YmdService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    val year = request.pathVariable("year").toInt()

    return ymdService.findMonths(type, year).collectList()
      .flatMap {
        if (it.isEmpty()) noContent().build() // 204
        else ok().contentType(APPLICATION_JSON).bodyValue(json.encodeToString(it)) // 200
      }
  }

  companion object {
    /** The default [RequestPredicate] */
    val REQUEST_PREDICATE: RequestPredicate = GET("/{type}/{year}/month")
  }
}