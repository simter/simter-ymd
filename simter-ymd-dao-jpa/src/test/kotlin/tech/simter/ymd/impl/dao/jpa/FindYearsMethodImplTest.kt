package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.TestHelper.nextType
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class FindYearsMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll()
    repository.flush()
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
    val t2y1 = randomYmd(type = nextType(), year = 2018) // another type
    val all = listOf(t1y1, t1y2, t2y1)
    repository.saveAll(all)
    repository.flush()

    // invoke and verify with desc order
    dao.findYears(type = t1y1.type).test()
      .expectNextSequence(listOf(t1y2, t1y1).map { it.year })
      .verifyComplete()
  }
}