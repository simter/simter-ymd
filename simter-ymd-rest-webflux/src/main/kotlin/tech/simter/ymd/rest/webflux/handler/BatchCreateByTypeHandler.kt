package tech.simter.ymd.rest.webflux.handler


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import reactor.core.publisher.Mono
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.impl.ImmutableYmd

/**
 * The [HandlerFunction] for batch create multiple [Ymd] records.
 *
 * @author RJ
 */
@Component
class BatchCreateByTypeHandler @Autowired constructor(
  private val service: YmdService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    return request.bodyToMono<List<Map<String, Any>>>()
      // convert to ymd
      .map { list ->
        list.map {
          ImmutableYmd(
            type = type,
            year = it["year"] as Int,
            month = (it["month"] as? Int) ?: 0,
            day = (it["day"] as? Int) ?: 0
          )
        }
      }
      // save
      .flatMap { service.create(*it.toTypedArray()) }
      // response
      .then(noContent().build())
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = POST("/{type}").and(contentType(APPLICATION_JSON))
  }
}