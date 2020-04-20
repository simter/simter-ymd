package tech.simter.ymd.impl.dao.r2dbc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.insert
import tech.simter.ymd.test.TestHelper.randomType
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class FindMonthsMethodImplTest @Autowired constructor(
  private val client: DatabaseClient,
  val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    dao.findMonths(type = randomType(), year = 2000)
      .test()
      .verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data
    val t1y1m1 = randomYmd(type = randomType(), year = 2001, month = 1)
    val t1y1m2 = randomYmd(type = t1y1m1.type, year = 2001, month = 2)
    val t1y2m = randomYmd(type = t1y1m1.type, year = 2002, month = 3) // another year
    val t2ym = randomYmd(type = randomType(), year = 2003, month = 4) // another type
    val all = listOf(t1y1m1, t1y1m2, t1y2m, t2ym)
    all.forEach { insert(client = client, ymd = it) }

    // invoke and verify with desc order
    dao.findMonths(type = t1y1m1.type, year = t1y1m1.year)
      .test()
      .expectNextSequence(listOf(t1y1m2, t1y1m1).map { it.month })
      .verifyComplete()
  }
}