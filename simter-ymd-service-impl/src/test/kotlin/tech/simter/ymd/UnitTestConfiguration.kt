package tech.simter.ymd

import com.ninjasquad.springmockk.MockkBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.service.ModuleConfiguration

/**
 * All test configuration for this module.
 *
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
@MockkBean(YmdDao::class, ModuleAuthorizer::class)
class UnitTestConfiguration