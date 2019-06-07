package tech.simter.ymd.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
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
  private val ymdService: YmdService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    val year = request.pathVariable("year").toInt()

    return ymdService.findMonths(type, year).collectList()
      .flatMap {
        if (it.isEmpty()) noContent().build() // 204
        else ok().contentType(APPLICATION_JSON).syncBody(it) // 200
      }
  }

  companion object {
    /** The default [RequestPredicate] */
    val REQUEST_PREDICATE: RequestPredicate = RequestPredicates.GET("/{type}/{year}/month")
  }
}