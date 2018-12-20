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
import java.time.MonthDay
import java.time.YearMonth

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
    val yearMonth = YearMonth.of(randomInt(1900, 3000), randomInt(1, 12))
    `when`(dao.findDays(type, yearMonth)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findDays(type, yearMonth)).verifyComplete()
    verify(dao).findDays(type, yearMonth)
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val yearMonth = YearMonth.of(randomInt(1900, 3000), randomInt(1, 12))
    val monthDay = MonthDay.of(yearMonth.month, randomInt(1, yearMonth.month.maxLength()))
    `when`(dao.findDays(type, yearMonth)).thenReturn(Flux.just(monthDay))

    // invoke and verify
    StepVerifier.create(service.findDays(type, yearMonth))
      .expectNext(monthDay)
      .verifyComplete()
    verify(dao).findDays(type, yearMonth)
  }
}