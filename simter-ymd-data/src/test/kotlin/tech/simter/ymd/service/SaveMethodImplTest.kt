package tech.simter.ymd.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class SaveMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Save nothing`() {
    // mock
    `when`(dao.save()).thenReturn(Mono.empty())

    // invoke and verify
    StepVerifier.create(service.save()).verifyComplete()
    verify(dao).save()
  }

  @Test
  fun `Save one`() {
    // mock
    val po = randomYmd()
    `when`(dao.save(po)).thenReturn(Mono.empty())

    // invoke and verify
    StepVerifier.create(service.save(po)).verifyComplete()
    verify(dao).save(po)
  }

  @Test
  fun `Save two`() {
    // mock
    val po1 = randomYmd()
    val po2 = randomYmd()
    `when`(dao.save(po1, po2)).thenReturn(Mono.empty())

    // invoke and verify
    StepVerifier.create(service.save(po1, po2)).verifyComplete()
    verify(dao).save(po1, po2)
  }
}