package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class FindYearsMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  private val type = randomString("type")
  @Test
  fun `Found nothing`() {
    StepVerifier.create(dao.findYears(type)).verifyComplete()
    assertEquals(0, repository.count())
  }

  @Test
  fun `Found something`() {
    // init data with asc order
    val expected = (1..3).map { randomYmd(type = type, year = 2000 + it, month = 0, day = 0) }
    repository.saveAll(expected)

    // init one other type
    repository.save(randomYmd(type = "${type}_not-same", month = 0, day = 0))

    // invoke
    val result = dao.findYears(type)

    // verify with desc order
    StepVerifier.create(result.collectList())
      .consumeNextWith {
        val lastIndex = expected.size - 1
        assertEquals(expected.size, it.size)
        expected.forEachIndexed { index, ymd -> assertEquals(ymd.year, it[lastIndex - index]) } // desc
      }.verifyComplete()
  }
}