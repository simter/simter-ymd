package tech.simter.ymd.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.ymd.service.YmdService

/**
 *  Find all years of the specific type [FindYearsHandler]
 *
 *  @author RJ
 */
@Component("tech.simter.ymd.rest.webflux.handler.FindYearsHandler")
class FindYearsHandler @Autowired constructor(
  private val ymdService: YmdService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    // get params
    val type = request.pathVariable("type")
    val withLatestYearMonths = request.queryParam("with-latest-year-months")
    val withLatestMonthDays = request.queryParam("with-latest-month-days")

    // find years
    val years = when {
      withLatestMonthDays.isPresent -> ymdService.findYearsWithLatestMonthDays(type)
      withLatestYearMonths.isPresent -> ymdService.findYearsWithLatestYearMonths(type)
      else -> ymdService.findYears(type)
    }

    // response
    return years.collectList()
      .flatMap {
        if (it.isEmpty()) noContent().build() // 204
        else ok().contentType(APPLICATION_JSON_UTF8).syncBody(it) // 200
      }
  }

  companion object {
    /** The default [RequestPredicate] */
    val REQUEST_PREDICATE: RequestPredicate = GET("/{type}/year")
  }
}