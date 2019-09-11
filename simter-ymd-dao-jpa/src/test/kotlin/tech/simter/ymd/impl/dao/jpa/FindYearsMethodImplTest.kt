package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomType
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class FindYearsMethodImplTest @Autowired constructor(
  val rem: TestEntityManager,
  val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    dao.findYears(type = randomType()).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1 = randomYmd(type = randomType(), year = 2000)
    val t1y2 = randomYmd(type = t1y1.type, year = 2001)
    val t2y1 = randomYmd(type = randomType(), year = 2018) // another type
    rem.persist(t1y1, t1y2, t2y1)

    // invoke and verify with desc order
    dao.findYears(type = t1y1.type).test()
      .expectNextSequence(listOf(t1y2, t1y1).map { it.year })
      .verifyComplete()
  }
}