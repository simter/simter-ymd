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
import tech.simter.ymd.PACKAGE
import tech.simter.ymd.rest.webflux.handler.*

/**
 * All configuration for this module.
 *
 * Register a `RouterFunction<ServerResponse>` with all routers for this module.
 * The default context-path of this router is '/ymd'. And can be config by property `simter-ymd.rest-context-path`.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.rest.webflux.ModuleConfiguration")
@ComponentScan
class ModuleConfiguration @Autowired constructor(
  @Value("\${simter-ymd.version:UNKNOWN}") private val version: String,
  @Value("\${simter-ymd.rest-context-path:/ymd}") private val contextPath: String,
  private val findDaysHandler: FindDaysHandler,
  private val findMonthsHandler: FindMonthsHandler,
  private val findYearsHandler: FindYearsHandler,
  private val createHandler: CreateHandler,
  private val batchCreateByTypeHandler: BatchCreateByTypeHandler
) {
  private val logger = LoggerFactory.getLogger(ModuleConfiguration::class.java)

  init {
    logger.warn("simter-ymd.rest-context-path='{}'", contextPath)
  }

  /** Register a `RouterFunction<ServerResponse>` with all routers for this module */
  @Bean("$PACKAGE.rest.webflux.Routes")
  @ConditionalOnMissingBean(name = ["$PACKAGE.rest.webflux.Routes"])
  fun simterYmdRoutes() = router {
    contextPath.nest {
      // GET /{type}/{year}/{month}/day    Find all days of the specific type, year and month
      FindDaysHandler.REQUEST_PREDICATE.invoke(findDaysHandler::handle)
      // GET /{type}/{year}/month          Find all months of the specific type and year
      FindMonthsHandler.REQUEST_PREDICATE.invoke(findMonthsHandler::handle)
      // GET /{type}/year                  Find all years of the specific type
      FindYearsHandler.REQUEST_PREDICATE.invoke(findYearsHandler::handle)
      // POST /{                           Create a new Ymd record
      CreateHandler.REQUEST_PREDICATE.invoke(createHandler::handle)
      // POST /{                           Batch create multiple Ymd records.
      BatchCreateByTypeHandler.REQUEST_PREDICATE.invoke(batchCreateByTypeHandler::handle)
      // GET /
      GET("/") { ok().contentType(TEXT_PLAIN).bodyValue("simter-ymd-rest-webflux-$version") }
    }
  }
}