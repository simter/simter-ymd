package tech.simter.ymd.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import tech.simter.ymd.service.YmdService
import java.time.Year
import java.time.format.DateTimeFormatter

/**
 * Find all months of the specific type and year [FindMonthsHandler]
 *
 * @author XA
 */
@Component("tech.simter.ymd.rest.webflux.handler.FindMonthsHandler")
class FindMonthsHandler @Autowired constructor(
  private val ymdService: YmdService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    val year = Year.parse(
      request.queryParam("y").get(), DateTimeFormatter.ofPattern("yyyy"))

    return ymdService.findMonths(type, year).collectList()
      .flatMap {
        if (it.isEmpty()) ServerResponse.noContent().build() // 204
        else ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(it) // 200
      }
  }

  companion object {
    /** The default [RequestPredicate] */
    val REQUEST_PREDICATE: RequestPredicate = RequestPredicates.GET("/{type}/month")
  }
}