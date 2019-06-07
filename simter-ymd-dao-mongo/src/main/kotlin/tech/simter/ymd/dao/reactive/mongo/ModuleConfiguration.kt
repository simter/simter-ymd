package tech.simter.ymd.dao.reactive.mongo

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
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
class ModuleConfiguration