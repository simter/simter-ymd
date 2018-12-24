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
    val t1y1m1d1 = randomYmd(type = type, year = 2015, month = 12, day = 25)
    val t1y1m1d2 = randomYmd(type = type, year = 2015, month = 12, day = 24)
    val t1y1m2d = randomYmd(type = type, year = 2015, month = 10)
    val t1y2md = randomYmd(type = type, year = 2019)
    val t2y3m3d3 = randomYmd(type = "ttt", year = 2011, month = 3, day = 2)
    repository.saveAll(listOf(t1y1m1d1, t1y1m1d2, t1y1m2d, t1y2md, t2y3m3d3))

    // invoke and verify
    StepVerifier.create(dao.findDays(type, YearMonth.of(t1y1m1d1.year, t1y1m1d1.month)))
      .expectNext(MonthDay.of(t1y1m1d1.month, t1y1m1d1.day))
      .expectNext(MonthDay.of(t1y1m1d2.month, t1y1m1d2.day))
      .verifyComplete()
    StepVerifier.create(dao.findDays(type, YearMonth.of(t1y2md.year, t1y2md.month)))
      .expectNext(MonthDay.of(t1y2md.month, t1y2md.day))
      .verifyComplete()
    StepVerifier.create(dao.findDays("ttt", YearMonth.of(t2y3m3d3.year, t2y3m3d3.month)))
      .expectNext(MonthDay.of(t2y3m3d3.month, t2y3m3d3.day))
      .verifyComplete()
  }
}