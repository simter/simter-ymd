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
import java.time.Month

/**
 * Test [YmdServiceImpl.findDays].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindDaysMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    every { dao.findDays(type, year, month) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findDays(type, year, month).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findDays(type, year, month)
    }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    val monthDay = randomInt(1, Month.of(month).maxLength())
    every { dao.findDays(type, year, month) } returns Flux.just(monthDay)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findDays(type, year, month).test()
      .expectNext(monthDay)
      .verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findDays(type, year, month)
    }
  }
}