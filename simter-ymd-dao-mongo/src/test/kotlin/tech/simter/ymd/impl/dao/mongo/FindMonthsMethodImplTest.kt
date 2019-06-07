package tech.simter.ymd.impl.dao.mongo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.mongo.TestHelper.nextId
import tech.simter.ymd.impl.dao.mongo.TestHelper.nextType
import tech.simter.ymd.impl.dao.mongo.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindMonthsMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  private val dao: YmdDao
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
    val t1y1m2 = randomYmd(type = t1y1m1.type, year = 2001, month = 2)
    val t1y1m2c = t1y1m2.copy(id = nextId())                          // duplicate with t1y1m2
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