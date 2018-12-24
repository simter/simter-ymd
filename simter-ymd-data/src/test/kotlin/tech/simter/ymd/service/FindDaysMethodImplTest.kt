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

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class FindDaysMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    `when`(dao.findDays(type, year, month)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findDays(type, year, month)).verifyComplete()
    verify(dao).findDays(type, year, month)
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    val monthDay = randomInt(1, Month.of(month).maxLength())
    `when`(dao.findDays(type, year, month)).thenReturn(Flux.just(monthDay))

    // invoke and verify
    StepVerifier.create(service.findDays(type, year, month))
      .expectNext(monthDay)
      .verifyComplete()
    verify(dao).findDays(type, year, month)
  }
}