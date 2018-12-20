package tech.simter.ymd.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.dao.YmdDao
import java.time.Year

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class FindYearsMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    `when`(dao.findYears(type)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findYears(type)).verifyComplete()
    verify(dao).findYears(type)
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = Year.of(randomInt(1900, 3000))
    `when`(dao.findYears(type)).thenReturn(Flux.just(year))

    // invoke and verify
    StepVerifier.create(service.findYears(type))
      .expectNext(year)
      .verifyComplete()
    verify(dao).findYears(type)
  }
}