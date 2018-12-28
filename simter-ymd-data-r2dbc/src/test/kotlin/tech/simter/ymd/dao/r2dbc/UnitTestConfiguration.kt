package tech.simter.ymd.dao.r2dbc

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration
@Import(ModuleConfiguration::class)
@EnableConfigurationProperties // for tech.simter.r2dbc.R2dbcProperties
class UnitTestConfiguration