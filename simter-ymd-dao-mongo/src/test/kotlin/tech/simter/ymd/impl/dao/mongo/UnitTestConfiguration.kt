package tech.simter.ymd.impl.dao.mongo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener

/**
 * All test configuration for this module.
 *
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
class UnitTestConfiguration {
  private val logger = LoggerFactory.getLogger(UnitTestConfiguration::class.java)
  @Bean
  @ConditionalOnProperty(name = ["simter.mongodb.enabled-logging-event-listener"], havingValue = "true")
  @ConditionalOnMissingBean
  fun mongoEventListener(): LoggingEventListener {
    logger.warn("instance a LoggingEventListener bean for mongodb operations")
    return LoggingEventListener()
  }
}