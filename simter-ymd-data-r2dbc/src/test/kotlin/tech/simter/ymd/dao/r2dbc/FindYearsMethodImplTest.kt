package tech.simter.ymd.dao.r2dbc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

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
  private val type = randomString("type")

  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Found nothing`() {
    dao.findYears(type).test().verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data with asc order
    val expected = (1..3).map { randomYmd(type = type, year = 2000 + it) }
    repository.saveAll(expected).test().expectNextCount(3).verifyComplete()

    // init one other type
    repository.save(randomYmd(type = "${type}_not-same")).test().expectNextCount(1).verifyComplete()

    // invoke and verify with desc order
    dao.findYears(type).collectList().test().consumeNextWith {
      val lastIndex = expected.size - 1
      assertEquals(expected.size, it.size)
      expected.forEachIndexed { index, ymd -> assertEquals(ymd.year, it[lastIndex - index]) } // desc
    }.verifyComplete()
  }
}