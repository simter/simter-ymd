package tech.simter.ymd.impl.dao.mongo

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import tech.simter.ymd.PACKAGE

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.impl.dao.mongo.ModuleConfiguration")
@EnableReactiveMongoRepositories
@ComponentScan
class ModuleConfiguration