package tech.simter.ymd.dao.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import tech.simter.ymd.PACKAGE_NAME

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE_NAME.dao.jpa.ModuleConfiguration")
@ComponentScan("$PACKAGE_NAME.dao.jpa")
@EnableJpaRepositories("$PACKAGE_NAME.dao.jpa")
@EntityScan("$PACKAGE_NAME.po")
class ModuleConfiguration