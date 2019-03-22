package tech.simter.ymd.service

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.MockkBeans
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.ymd.OPERATION_SAVE
import tech.simter.ymd.PACKAGE_NAME
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

@SpringJUnitConfig(YmdServiceImpl::class)
@MockkBeans(
  MockkBean(YmdDao::class),
  MockkBean(ModuleAuthorizer::class, name = "$PACKAGE_NAME.service.ModuleAuthorizer")
)
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