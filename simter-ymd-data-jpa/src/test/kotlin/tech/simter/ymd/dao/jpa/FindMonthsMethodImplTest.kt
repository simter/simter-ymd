package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomInt
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao
import java.time.Year

/**
 * Test [YmdDaoImpl.findMonths]
 *
 * @author RJ
 * @author XA
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class FindMonthsMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  @Test
  fun `Found nothing`() {
    StepVerifier.create(dao.findMonths("type1", Year.now())).verifyComplete()
  }

  @Test
  fun `Found something`() {
    // init data with specific type year and month
    val t1 = "t1"
    val t2 = "t2"                              // another type
    val y1 = Year.of(randomInt(2000, 3000))
    val y2 = y1.minusYears(1) // another year
    val t1y1Ymds = (12 downTo 6).map { randomYmd(type = t1, year = y1.value, month = it) }
    val t1y2Ymds = (5 downTo 1).map { randomYmd(type = t1, year = y2.value, month = it) }
    val t2y1Ymds = t1y1Ymds.map { it.copy(type = t2, month = it.month - 1) }
    val t2y2Ymds = t1y2Ymds.map { it.copy(type = t2, month = it.month + 2) }

    // save init data
    val allYmds = t1y1Ymds.toMutableList()
    allYmds.addAll(t1y2Ymds)
    allYmds.addAll(t2y1Ymds)
    allYmds.addAll(t2y2Ymds)
    repository.saveAll(allYmds)

    // invoke and verify t1y1
    StepVerifier.create(dao.findMonths(t1, y1).collectList())
      .consumeNextWith {
        assertEquals(t1y1Ymds.size, it.size)
        t1y1Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.month, it[index].value) }
      }.verifyComplete()

    // invoke and verify t1y2
    StepVerifier.create(dao.findMonths(t1, y2).collectList())
      .consumeNextWith {
        assertEquals(t1y2Ymds.size, it.size)
        t1y2Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.month, it[index].value) }
      }.verifyComplete()

    // invoke and verify t2y1
    StepVerifier.create(dao.findMonths(t2, y1).collectList())
      .consumeNextWith {
        assertEquals(t2y1Ymds.size, it.size)
        t2y1Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.month, it[index].value) }
      }.verifyComplete()

    // invoke and verify t2y2
    StepVerifier.create(dao.findMonths(t2, y2).collectList())
      .consumeNextWith {
        assertEquals(t2y2Ymds.size, it.size)
        t2y2Ymds.forEachIndexed { index, ymd -> assertEquals(ymd.month, it[index].value) }
      }.verifyComplete()
  }
}