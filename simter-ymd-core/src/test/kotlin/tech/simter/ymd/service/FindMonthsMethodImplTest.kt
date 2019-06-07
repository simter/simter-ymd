package tech.simter.ymd.service

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.MockkBeans
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.OPERATION_READ
import tech.simter.ymd.PACKAGE_NAME
import tech.simter.ymd.dao.YmdDao

@SpringJUnitConfig(YmdServiceImpl::class)
@MockkBeans(
  MockkBean(YmdDao::class),
  MockkBean(ModuleAuthorizer::class, name = "$PACKAGE_NAME.service.ModuleAuthorizer")
)
class FindMonthsMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    every { dao.findMonths(type, year) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findMonths(type, year).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findMonths(type, year)
    }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    every { dao.findMonths(type, year) } returns Flux.just(month)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findMonths(type, year).test()
      .expectNext(month)
      .verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findMonths(type, year)
    }
  }
}