package tech.simter.ymd.dao.r2dbc

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import tech.simter.ymd.PACKAGE_NAME

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE_NAME.dao.r2dbc.ModuleConfiguration")
@EnableR2dbcRepositories("$PACKAGE_NAME.dao.r2dbc")
@ComponentScan("$PACKAGE_NAME.dao.r2dbc", "tech.simter.r2dbc")
class ModuleConfiguration