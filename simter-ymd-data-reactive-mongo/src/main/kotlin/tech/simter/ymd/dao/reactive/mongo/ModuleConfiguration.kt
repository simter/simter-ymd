package tech.simter.ymd.dao.reactive.mongo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import tech.simter.ymd.PACKAGE_NAME

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE_NAME.dao.reactive.mongo.ModuleConfiguration")
@EnableReactiveMongoRepositories("$PACKAGE_NAME.dao.reactive.mongo")
@ComponentScan("$PACKAGE_NAME.dao.reactive.mongo")
@EntityScan("$PACKAGE_NAME.po")
class ModuleConfiguration {
  private val logger = LoggerFactory.getLogger(ModuleConfiguration::class.java)
  @Bean
  @ConditionalOnProperty(name = ["simter.mongodb.enabled-logging-event-listener"], havingValue = "true")
  @ConditionalOnMissingBean
  fun mongoEventListener(): LoggingEventListener {
    logger.warn("instance a LoggingEventListener bean for mongodb operations")
    return LoggingEventListener()
  }
}