package tech.simter.ymd

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.reactive.security.ReactiveSecurityService
import tech.simter.reactive.security.properties.LogicStrategy
import tech.simter.reactive.security.properties.ModuleAuthorizeProperties
import tech.simter.reactive.security.properties.PermissionStrategy
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.impl.service.ModuleConfiguration
import java.util.*

/**
 * See `application.yml` key `module.authorization.simter-ymd` value.
 */
@SpringBootTest(classes = [ModuleConfiguration::class])
@MockkBean(YmdDao::class, ReactiveSecurityService::class)
class ModuleAuthorizerConfigTest @Autowired constructor(
  private val properties: ModuleAuthorizeProperties,
  private val securityService: ReactiveSecurityService,
  @Qualifier("$PACKAGE.ModuleAuthorizer") private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun test() {
    assertNotNull(moduleAuthorizer)
    assertEquals(3, properties.operations.size)

    // 1. default permission
    assertEquals(PermissionStrategy.Deny, properties.defaultPermission)

    // 2. not-exists
    assertNull(properties.operations["NONE"])

    // 3. delete
    var operation = properties.operations.getValue(OPERATION_DELETE)
    assertEquals(LogicStrategy.And, operation.strategy)
    assertEquals(listOf("DELETER", "MANAGER"), operation.roles)

    // 4. save
    operation = properties.operations.getValue(OPERATION_SAVE)
    assertEquals(LogicStrategy.Or, operation.strategy)
    assertEquals(listOf("MANAGER"), operation.roles)

    // 5. read
    operation = properties.operations.getValue(OPERATION_READ)
    assertEquals(LogicStrategy.Or, operation.strategy)
    assertEquals(listOf("READER"), operation.roles)

    // 5.1 mock
    val type = UUID.randomUUID().toString()
    val year = 2000
    every { securityService.verifyHasAnyRole("READER") } returns Mono.empty()
    every { dao.findYears(type) } returns Flux.fromIterable(listOf(year))

    // 5.2 invoke and verify
    service.findYears(type).test().expectNext(year).verifyComplete()
    verify(exactly = 1) {
      securityService.verifyHasAnyRole("READER")
      dao.findYears(type)
    }
  }
}