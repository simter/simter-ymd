package tech.simter.ymd.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.OPERATION_SAVE
import tech.simter.ymd.UnitTestConfiguration
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import tech.simter.ymd.impl.service.TestHelper.randomYmd

/**
 * Test [YmdServiceImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class SaveMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Save nothing`() {
    // mock
    every { dao.save() } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_SAVE) } returns Mono.empty()

    // invoke and verify
    service.save().test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_SAVE)
      dao.save()
    }
  }

  @Test
  fun `Save one`() {
    // mock
    val po = randomYmd()
    every { dao.save(po) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_SAVE) } returns Mono.empty()

    // invoke and verify
    service.save(po).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_SAVE)
      dao.save(po)
    }
  }

  @Test
  fun `Save two`() {
    // mock
    val po1 = randomYmd()
    val po2 = randomYmd()
    every { dao.save(po1, po2) } returns Mono.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_SAVE) } returns Mono.empty()

    // invoke and verify
    service.save(po1, po2).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_SAVE)
      dao.save(po1, po2)
    }
  }
}