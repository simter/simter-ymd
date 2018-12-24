package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao
import java.time.Month
import java.time.Year

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author XA
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class FindMonthsMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  private val type = randomString("type")
  private val year = Year.now()

  @Test
  fun `Found nothing`() {
    StepVerifier.create(dao.findMonths(type, year)).verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data with specific type year and month
    val t1y1m1 = randomYmd(type = type, year = 2018, month = 12)
    val t1y1m2 = randomYmd(type = type, year = 2018, month = 11)
    val t1y2m1 = randomYmd(type = type, year = 2017, month = 10)
    val t1y2m2 = randomYmd(type = type, year = 2017, month = 9)
    val t2y3m3 = randomYmd(type = "yyy", year = 2015, month = 2)

    // save data
    repository.saveAll(listOf(t1y1m1, t1y1m2, t1y2m1, t1y2m2, t2y3m3))

    // invoke and verify
    StepVerifier.create(dao.findMonths(type, Year.of(t1y1m1.year)))
      .expectNext(Month.of(t1y1m1.month))
      .expectNext(Month.of(t1y1m2.month))
      .verifyComplete()
    StepVerifier.create(dao.findMonths(type, Year.of(t1y2m1.year)))
      .expectNext(Month.of(t1y2m1.month))
      .expectNext(Month.of(t1y2m2.month))
      .verifyComplete()
    StepVerifier.create(dao.findMonths("yyy", Year.of(t2y3m3.year)))
      .expectNext(Month.of(t2y3m3.month))
      .verifyComplete()
  }
}
