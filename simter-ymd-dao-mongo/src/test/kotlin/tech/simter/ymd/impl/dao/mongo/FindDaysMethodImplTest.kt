package tech.simter.ymd.impl.dao.mongo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.mongo.TestHelper.nextId
import tech.simter.ymd.impl.dao.mongo.po.YmdPo
import tech.simter.ymd.test.TestHelper.randomType
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findDays]
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindDaysMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  private val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Found nothing`() {
    dao.findDays(type = randomType(), year = 2000, month = 1).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1d1 = YmdPo.from(randomYmd(type = randomType(), year = 2001, month = 1, day = 1))
    val t1y1m1d2 = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2001, month = 1, day = 2))
    val t1y1m1d2c = t1y1m1d2.copy(id = nextId())                                   // duplicate with t1y1m1d2
    val t1y1m2d = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2001, month = 2, day = 3)) // another month
    val t1y2md = YmdPo.from(randomYmd(type = t1y1m1d1.type, year = 2002, month = 3, day = 4))  // another year
    val t2ymd = YmdPo.from(randomYmd(type = randomType(), year = 2003, month = 4, day = 5))      // another type
    val all = listOf(t1y1m1d1, t1y1m1d2, t1y1m1d2c, t1y1m2d, t1y2md, t2ymd)
    repository.saveAll(all).test()
      .expectNextCount(all.size.toLong())
      .verifyComplete()

    // invoke and verify with desc order
    dao.findDays(type = t1y1m1d1.type, year = t1y1m1d1.year, month = t1y1m1d1.month).test()
      .expectNextSequence(listOf(t1y1m1d2, t1y1m1d1).map { it.day })
      .verifyComplete()
  }
}