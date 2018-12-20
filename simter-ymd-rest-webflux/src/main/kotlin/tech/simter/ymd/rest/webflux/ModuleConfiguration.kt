package tech.simter.ymd.rest.webflux

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.web.reactive.function.server.router
import tech.simter.ymd.rest.webflux.handler.FindYearsHandler

private const val MODULE = "tech.simter.ymd.rest.webflux"

/**
 * All configuration for this module.
 *
 * Register a `RouterFunction<ServerResponse>` with all routers for this module.
 * The default context-path of this router can be config by property `module.rest-context-path.simter-ymd`.
 *
 * @author RJ
 */
@Configuration("$MODULE.ModuleConfiguration")
@ComponentScan(MODULE)
class ModuleConfiguration @Autowired constructor(
  @Value("\${module.version.simter-ymd:UNKNOWN}") private val version: String,
  @Value("\${module.rest-context-path.simter-ymd:/ymd}") private val contextPath: String,
  private val findYearsHandler: FindYearsHandler
) {
  private val logger = LoggerFactory.getLogger(ModuleConfiguration::class.java)

  init {
    logger.warn("module.version.simter-ymd='{}'", version)
    logger.warn("module.rest-context-path.simter-ymd='{}'", contextPath)
  }

  /** Register a `RouterFunction<ServerResponse>` with all routers for this module */
  @Bean("$MODULE.Routes")
  @ConditionalOnMissingBean(name = ["$MODULE.Routes"])
  fun simterYmdRoutes() = router {
    contextPath.nest {
      // GET /{type}/year  Find all years of the specific type
      FindYearsHandler.REQUEST_PREDICATE.invoke(findYearsHandler::handle)
      // GET /
      GET("/") { ok().contentType(TEXT_PLAIN).syncBody("simter-ymd-$version") }
    }
  }
}