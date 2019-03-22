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

@SpringJUnitConfig(YmdServiceImpl::class)
@MockkBean(YmdDao::class)
class FindMonthsMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    every { dao.findMonths(type, year) } returns Flux.empty()

    // invoke and verify
    service.findMonths(type, year).test().verifyComplete()
    verify(exactly = 1) { dao.findMonths(type, year) }
  }

  @Test
  fun `Found something`() {
    // mock
    val type = randomString()
    val year = randomInt(1900, 3000)
    val month = randomInt(1, 12)
    every { dao.findMonths(type, year) } returns Flux.just(month)

    // invoke and verify
    service.findMonths(type, year).test()
      .expectNext(month)
      .verifyComplete()
    verify(exactly = 1) { dao.findMonths(type, year) }
  }
}