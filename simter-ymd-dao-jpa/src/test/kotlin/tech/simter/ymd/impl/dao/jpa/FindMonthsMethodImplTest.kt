package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomType
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class FindMonthsMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    dao.findMonths(type = randomType(), year = 2000).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1 = randomYmd(type = randomType(), year = 2001, month = 1)
    val t1y1m2 = randomYmd(type = t1y1m1.type, year = 2001, month = 2)
    val t1y2m = randomYmd(type = t1y1m1.type, year = 2002, month = 3) // another year
    val t2ym = randomYmd(type = randomType(), year = 2003, month = 4) // another type
    rem.persist(t1y1m1, t1y1m2, t1y2m, t2ym)

    // invoke and verify with desc order
    dao.findMonths(type = t1y1m1.type, year = t1y1m1.year).test()
      .expectNextSequence(listOf(t1y1m2, t1y1m1).map { it.month })
      .verifyComplete()
  }
}