package tech.simter.ymd.dao.r2dbc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import tech.simter.util.RandomUtils
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao
import java.time.YearMonth

/**
 * Test [YmdDaoImpl.findDays]
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class FindDaysMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Found nothing`() {
    val ym = YearMonth.now()
    dao.findDays("type1", ym.year, ym.monthValue).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data with specific type, year, month and day
    val t1 = "t1"
    val t2 = "t2"                                                  // another type
    val y1m1 = YearMonth.of(RandomUtils.randomInt(2000, 3000), RandomUtils.randomInt(1, 11))
    val y1m2 = y1m1.plusMonths(1)                   // same year but another month
    val y2m1 = YearMonth.of(y1m1.year.plus(1), y1m1.month)  // another year month
    val t1y1m1Ymds = (20 downTo 15).map { randomYmd(type = t1, year = y1m1.year, month = y1m1.monthValue, day = it) }
    val t1y1m2Ymds = (14 downTo 10).map { randomYmd(type = t1, year = y1m2.year, month = y1m2.monthValue, day = it) }
    val t1y2m1Ymds = (9 downTo 1).map { randomYmd(type = t1, year = y2m1.year, month = y2m1.monthValue, day = it) }
    val t2y1m1Ymds = t1y1m1Ymds.map { it.copy(type = t2) }

    // save init data
    val allYmds = t1y1m1Ymds.toMutableList()
    allYmds.addAll(t1y1m2Ymds)
    allYmds.addAll(t1y2m1Ymds)
    allYmds.addAll(t2y1m1Ymds)
    repository.saveAll(allYmds).test().expectNextCount(allYmds.size.toLong()).verifyComplete()

    // invoke and verify t1y1m1
    dao.findDays(t1, y1m1.year, y1m1.monthValue).collectList().test().consumeNextWith {
      assertEquals(t1y1m1Ymds.size, it.size)
      t1y1m1Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.day, it[index]) }
    }.verifyComplete()

    // invoke and verify t1y1m2
    dao.findDays(t1, y1m2.year, y1m2.monthValue).collectList().test().consumeNextWith {
      assertEquals(t1y1m2Ymds.size, it.size)
      t1y1m2Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.day, it[index]) }
    }.verifyComplete()

    // invoke and verify t1y2m1
    dao.findDays(t1, y2m1.year, y2m1.monthValue).collectList().test().consumeNextWith {
      assertEquals(t1y2m1Ymds.size, it.size)
      t1y2m1Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.day, it[index]) }
    }.verifyComplete()

    // invoke and verify t2y1m1
    dao.findDays(t2, y1m1.year, y1m1.monthValue).collectList().test().consumeNextWith {
      assertEquals(t2y1m1Ymds.size, it.size)
      t2y1m1Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.day, it[index]) }
    }.verifyComplete()
  }
}