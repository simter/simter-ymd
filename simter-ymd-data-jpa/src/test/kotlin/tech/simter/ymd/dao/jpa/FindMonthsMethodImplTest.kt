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
    // init data with specific year and month
    val y1m1 = randomYmd(type = type, year = 2018, month = 12)
    val y1m2 = randomYmd(type = type, year = 2018, month = 11)
    val y2m1 = randomYmd(type = type, year = 2017, month = 10)
    val y2m2 = randomYmd(type = type, year = 2017, month = 9)

    // save data
    repository.saveAll(listOf(y1m1,y1m2,y2m1,y2m2))

    // invoke and verify
    StepVerifier.create(dao.findMonths(type, Year.of(y1m1.year)))
      .expectNext(Month.of(y1m1.month))
      .expectNext(Month.of(y1m2.month))
      .verifyComplete()
    StepVerifier.create(dao.findMonths(type, Year.of(y2m1.year)))
      .expectNext(Month.of(y2m1.month))
      .expectNext(Month.of(y2m2.month))
      .verifyComplete()
  }
}
