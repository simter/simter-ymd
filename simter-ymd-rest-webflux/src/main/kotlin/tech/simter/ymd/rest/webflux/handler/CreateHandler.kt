package tech.simter.ymd.rest.webflux.handler


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import reactor.core.publisher.Mono
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdService

/**
 * The [HandlerFunction] for create a new [Ymd] record.
 *
 * @author RJ
 */
@Component
class CreateHandler @Autowired constructor(
  private val json: Json,
  private val service: YmdService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return request.bodyToMono<String>()
      .switchIfEmpty(Mono.error(IllegalStateException("Empty body!")))
      // convert to ymd
      .map { json.decodeFromString(it) as Ymd }
      // save
      .flatMap { service.create(it) }
      // response
      .then(noContent().build())
      .onErrorResume(IllegalStateException::class.java) {
        badRequest().contentType(MediaType.valueOf("$TEXT_PLAIN_VALUE;charset=UTF-8")).bodyValue(it.message ?: "")
      }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = POST("/").and(contentType(APPLICATION_JSON))
  }
}