package tech.simter.ymd.impl.dao.r2dbc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.nextType
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.randomYmd

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
    dao.findDays(type = nextType(), year = 2000, month = 1).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1d1 = randomYmd(type = nextType(), year = 2001, month = 1, day = 1)
    val t1y1m1d2 = randomYmd(type = t1y1m1d1.type, year = 2001, month = 1, day = 2)
    val t1y1m2d = randomYmd(type = t1y1m1d1.type, year = 2001, month = 2, day = 3) // another month
    val t1y2md = randomYmd(type = t1y1m1d1.type, year = 2002, month = 3, day = 4)  // another year
    val t2ymd = randomYmd(type = nextType(), year = 2003, month = 4, day = 5)      // another type
    val all = listOf(t1y1m1d1, t1y1m1d2, t1y1m2d, t1y2md, t2ymd)
    repository.saveAll(all).test()
      .expectNextCount(all.size.toLong())
      .verifyComplete()

    // invoke and verify with desc order
    dao.findDays(type = t1y1m1d1.type, year = t1y1m1d1.year, month = t1y1m1d1.month).test()
      .expectNextSequence(listOf(t1y1m1d2, t1y1m1d1).map { it.day })
      .verifyComplete()
  }
}