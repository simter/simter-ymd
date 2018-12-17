package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao
import java.time.MonthDay
import java.time.YearMonth

/**
 * Test [YmdDaoImpl.findDays]
 *
 * @author XA
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class FindDaysMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  private val type = randomString("type")
  private val yearMonth = YearMonth.now()
  @Test
  fun `Found nothing`() {
    StepVerifier.create(dao.findDays(type, yearMonth)).verifyComplete()
    assertEquals(0, repository.count())
  }

  @Test
  fun `Found something`() {
    // init data with specific year、month and day
    val y1m1d1 = randomYmd(type = type, year = 2015, month = 12, day = 25)
    val y1m1d2 = randomYmd(type = type, year = 2015, month = 12, day = 24)
    val y1m2d = randomYmd(type = type, year = 2015, month = 10)
    val y2md = randomYmd(type = type, year = 2019)
    repository.saveAll(listOf(y1m1d1, y1m1d2, y1m2d, y2md))

    // invoke and verify
    StepVerifier.create(dao.findDays(type, YearMonth.of(y1m1d1.year, y1m1d1.month)))
      .expectNext(MonthDay.of(y1m1d1.month, y1m1d1.day))
      .expectNext(MonthDay.of(y1m1d2.month, y1m1d2.day))
      .verifyComplete()
    StepVerifier.create(dao.findDays(type, YearMonth.of(y2md.year, y2md.month)))
      .expectNext(MonthDay.of(y2md.month, y2md.day))
      .verifyComplete()
  }
}