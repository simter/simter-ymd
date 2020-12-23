package tech.simter.ymd.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.insert
import tech.simter.ymd.test.TestHelper.randomType
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findYears].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataR2dbcTest
class FindYearsMethodImplTest @Autowired constructor(
  private val client: DatabaseClient,
  private val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    dao.findYears(type = randomType())
      .test()
      .verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1 = randomYmd(type = randomType(), year = 2000)
    val t1y2 = randomYmd(type = t1y1.type, year = 2001)
    val t2y1 = randomYmd(type = randomType(), year = 2003) // another type
    val all = listOf(t1y1, t1y2, t2y1)
    all.forEach { insert(client = client, ymd = it) }

    // invoke and verify with desc order
    dao.findYears(type = t1y1.type)
      .test()
      .expectNextSequence(listOf(t1y2, t1y1).map { it.year })
      .verifyComplete()
  }
}