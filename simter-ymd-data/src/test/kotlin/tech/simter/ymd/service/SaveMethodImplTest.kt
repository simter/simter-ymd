package tech.simter.ymd.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.test
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

@SpringJUnitConfig(YmdServiceImpl::class)
@MockkBean(YmdDao::class)
class SaveMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Save nothing`() {
    // mock
    every { dao.save() } returns Mono.empty()

    // invoke and verify
    service.save().test().verifyComplete()
    verify(exactly = 1) { dao.save() }
  }

  @Test
  fun `Save one`() {
    // mock
    val po = randomYmd()
    every { dao.save(po) } returns Mono.empty()

    // invoke and verify
    service.save(po).test().verifyComplete()
    verify(exactly = 1) { dao.save(po) }
  }

  @Test
  fun `Save two`() {
    // mock
    val po1 = randomYmd()
    val po2 = randomYmd()
    every { dao.save(po1, po2) } returns Mono.empty()

    // invoke and verify
    service.save(po1, po2).test().verifyComplete()
    verify(exactly = 1) { dao.save(po1, po2) }
  }
}