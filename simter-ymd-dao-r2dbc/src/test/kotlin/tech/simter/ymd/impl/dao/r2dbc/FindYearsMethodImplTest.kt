package tech.simter.ymd.impl.dao.r2dbc

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.nextType
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.findYears].
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class FindYearsMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  val dao: YmdDao
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
    val t1y2 = randomYmd(type = t1y1.type, year = 2001, day = 1)
    val t2y1 = randomYmd(type = nextType(), year = 2018) // another type
    val all = listOf(t1y1, t1y2, t2y1)
    repository.saveAll(all).test()
      .expectNextCount(all.size.toLong())
      .verifyComplete()

    // invoke and verify with desc order
    dao.findYears(type = t1y1.type).test()
      .expectNextSequence(listOf(t1y2, t1y1).map { it.year })
      .verifyComplete()
  }
}