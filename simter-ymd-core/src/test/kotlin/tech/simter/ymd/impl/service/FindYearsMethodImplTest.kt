package tech.simter.ymd.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.OPERATION_READ
import tech.simter.ymd.UnitTestConfiguration
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService

/**
 * Test [YmdServiceImpl.findYears].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindYearsMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    every { dao.findYears(type) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findYears(type).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findYears(type)
    }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    every { dao.findYears(type) } returns Flux.just(year)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findYears(type).test().expectNext(year).verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findYears(type)
    }
  }
}