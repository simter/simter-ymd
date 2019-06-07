package tech.simter.ymd.impl.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService
import tech.simter.reactive.security.properties.ModuleAuthorizeProperties
import tech.simter.reactive.security.properties.PermissionStrategy.Allow
import tech.simter.ymd.PACKAGE

/**
 * All configuration for this module.
 *
 * @author RJ
 */
@Configuration("$PACKAGE.impl.service.ModuleConfiguration")
@EnableConfigurationProperties
@ComponentScan
class ModuleConfiguration {
  /**
   * Starter should config yml key `module.authorization.simter-ymd` to support specific role control,
   * otherwise the [ModuleConfiguration.moduleAuthorizer] would allow anything default.
   */
  @Bean("$PACKAGE.ModuleAuthorizeProperties")
  @ConfigurationProperties(prefix = "module.authorization.simter-ymd")
  fun moduleAuthorizeProperties(): ModuleAuthorizeProperties {
    return ModuleAuthorizeProperties(defaultPermission = Allow)
  }

  @Bean("$PACKAGE.ModuleAuthorizer")
  fun moduleAuthorizer(
    @Qualifier("$PACKAGE.ModuleAuthorizeProperties")
    properties: ModuleAuthorizeProperties,
    securityService: ReactiveSecurityService
  ): ModuleAuthorizer {
    return ModuleAuthorizer.create(properties, securityService)
  }
}