package tech.simter.ymd.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.ymd.service.YmdService
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * Find all days of the specific type, year and month [FindDaysHandler]
 *
 * @author XA
 */

@Component("tech.simter.ymd.rest.webflux.handler.FindDaysHandler")
class FindDaysHandler @Autowired constructor(
  private val ymdService: YmdService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    val yearMonth = YearMonth.parse(
      request.queryParam("ym").orElseGet(null), DateTimeFormatter.ofPattern("yyyyMM"))

    return ymdService.findDays(type, yearMonth)
      .collectList()
      .flatMap {
        if (it.isEmpty()) ServerResponse.noContent().build() // 204
        else ok().contentType(APPLICATION_JSON_UTF8).syncBody(it) // 200
      }
  }

  companion object {
    /** The default [RequestPredicate] */
    val REQUEST_PREDICATE: RequestPredicate = RequestPredicates.GET("/{type}/day")
  }
}