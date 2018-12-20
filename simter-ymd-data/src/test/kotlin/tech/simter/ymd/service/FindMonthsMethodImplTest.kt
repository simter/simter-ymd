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
import java.time.Month
import java.time.Year

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class FindMonthsMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = Year.of(randomInt(1900, 3000))
    `when`(dao.findMonths(type, year)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findMonths(type, year)).verifyComplete()
    verify(dao).findMonths(type, year)
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = Year.of(randomInt(1900, 3000))
    val month = Month.of(randomInt(1, 12))
    `when`(dao.findMonths(type, year)).thenReturn(Flux.just(month))

    // invoke and verify
    StepVerifier.create(service.findMonths(type, year))
      .expectNext(month)
      .verifyComplete()
    verify(dao).findMonths(type, year)
  }
}