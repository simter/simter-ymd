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
 * Test [YmdDaoImpl.findYears].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class FindYearsMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  private val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Found nothing`() {
    dao.findYears(type = nextType()).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1 = randomYmd(type = nextType(), year = 2000)
    val t1y2 = randomYmd(type = t1y1.type, year = 2001)
    val t1y2c = t1y2.copy(id = nextId())                 // duplicate with t1y2
    val t2y1 = randomYmd(type = nextType(), year = 2018) // another type
    val all = listOf(t1y1, t1y2, t1y2c, t2y1)
    repository.saveAll(all).test()
      .expectNextCount(all.size.toLong())
      .verifyComplete()

    // invoke and verify with desc order
    dao.findYears(type = t1y1.type).test()
      .expectNextSequence(listOf(t1y2, t1y1).map { it.year })
      .verifyComplete()
  }
}