package tech.simter.ymd.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.UnitTestConfiguration
import tech.simter.ymd.core.Ymd.Companion.OPERATION_EDIT
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdServiceImpl.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class CreateTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Create nothing`() {
    // mock
    every { dao.create() } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_EDIT) } returns Mono.empty()

    // invoke and verify
    service.create().test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_EDIT)
      dao.create()
    }
  }

  @Test
  fun `Create one`() {
    // mock
    val po = randomYmd()
    every { dao.create(po) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_EDIT) } returns Mono.empty()

    // invoke and verify
    service.create(po).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_EDIT)
      dao.create(po)
    }
  }

  @Test
  fun `Create two`() {
    // mock
    val po1 = randomYmd()
    val po2 = randomYmd()
    every { dao.create(po1, po2) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_EDIT) } returns Mono.empty()

    // invoke and verify
    service.create(po1, po2).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_EDIT)
      dao.create(po1, po2)
    }
  }
}