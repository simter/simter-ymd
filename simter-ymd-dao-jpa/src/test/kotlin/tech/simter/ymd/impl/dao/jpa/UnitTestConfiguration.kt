package tech.simter.ymd.impl.dao.jpa

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author RJ
 */
@Configuration
@Import(
  tech.simter.reactive.jpa.ModuleConfiguration::class,
  tech.simter.reactive.test.ModuleConfiguration::class,
  tech.simter.ymd.impl.dao.jpa.ModuleConfiguration::class
)
class UnitTestConfiguration