package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.po.YmdPo
import tech.simter.ymd.test.TestHelper.randomType
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findDays]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class FindDaysMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    dao.findDays(type = randomType(), year = 2000, month = 1).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1d1 = YmdPo.from(randomYmd(type = randomType(), year = 2001, month = 1, day = 1))
    val t1y1m1d2 = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2001, month = 1, day = 2))
    val t1y1m2d = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2001, month = 2, day = 3)) // another month
    val t1y2md = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2002, month = 3, day = 4))  // another year
    val t2ymd = YmdPo.from(randomYmd(type = randomType(), year = 2003, month = 4, day = 5))    // another type
    rem.persist(t1y1m1d1, t1y1m1d2, t1y1m2d, t1y2md, t2ymd)

    // invoke and verify with desc order
    dao.findDays(type = t1y1m1d1.type, year = t1y1m1d1.year, month = t1y1m1d1.month).test()
      .expectNextSequence(listOf(t1y1m1d2, t1y1m1d1).map { it.day })
      .verifyComplete()
  }
}