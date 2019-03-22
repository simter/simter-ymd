package tech.simter.ymd.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.test
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.dao.YmdDao
import java.time.Month

@SpringJUnitConfig(YmdServiceImpl::class)
@MockkBean(YmdDao::class)
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
    every { dao.findDays(type, year, month) } returns Flux.empty()

    // invoke and verify
    service.findDays(type, year, month).test().verifyComplete()
    verify(exactly = 1) { dao.findDays(type, year, month) }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    val monthDay = randomInt(1, Month.of(month).maxLength())
    every { dao.findDays(type, year, month) } returns Flux.just(monthDay)

    // invoke and verify
    service.findDays(type, year, month).test()
      .expectNext(monthDay)
      .verifyComplete()
    verify(exactly = 1) { dao.findDays(type, year, month) }
  }
}