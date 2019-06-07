package tech.simter.ymd.dao.r2dbc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import tech.simter.ymd.TestUtils.nextId
import tech.simter.ymd.TestUtils.nextType
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class FindMonthsMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Found nothing`() {
    dao.findMonths(type = nextType(), year = 2000).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1 = randomYmd(type = nextType(), year = 2001, month = 1)
    val t1y1m2 = randomYmd(type = t1y1m1.type, year = 2001, month = 2, day = 1)
    val t1y1m2c = t1y1m2.copy(id = nextId(), day = 2)                 // duplicate year, month with t1y1m2
    val t1y2m = randomYmd(type = t1y1m1.type, year = 2002, month = 3) // another year
    val t2ym = randomYmd(type = nextType(), year = 2003, month = 4)   // another type
    val all = listOf(t1y1m1, t1y1m2, t1y1m2c, t1y2m, t2ym)
    repository.saveAll(all).test()
      .expectNextCount(all.size.toLong())
      .verifyComplete()

    // invoke and verify with desc order
    dao.findMonths(type = t1y1m1.type, year = t1y1m1.year).test()
      .expectNextSequence(listOf(t1y1m2, t1y1m1).map { it.month })
      .verifyComplete()
  }
}