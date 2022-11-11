package tech.simter.ymd.rest.webflux.handler


import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

/**
 * The [HandlerFunction] for batch create multiple [Ymd] records.
 *
 * @author RJ
 */
@Component
class BatchCreateByTypeHandler @Autowired constructor(
  private val json: Json,
  private val service: YmdService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val type = request.pathVariable("type")
    return request.bodyToMono<String>()
      // convert to ymd
      .map { jsonArrayStr ->
        val list: List<YmdWithoutType> = json.decodeFromString(jsonArrayStr)
        list.map {
          Ymd(
            type = type,
            year = it.year,
            month = it.month,
            day = it.day,
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

  @Serializable
  private data class YmdWithoutType(
    /** 4 digits year, such as 2018 */
    val year: Int,
    /** month from 1 to 12. 0 means ignored */
    val month: Int = 0,
    /** day from 1 to 31. 0 means ignored */
    val day: Int = 0,
  )
}