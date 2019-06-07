package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataJpaTest
class SaveMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll()
    repository.flush()
  }

  @Test
  fun `Save nothing`() {
    dao.save().test().verifyComplete()
    assertEquals(0, repository.count())
  }

  @Test
  fun `Save one`() {
    // init data
    val po = randomYmd()

    // invoke and verify
    dao.save(po).test().verifyComplete()
    assertEquals(po, repository.getOne(po.id))
    repository.flush()
  }

  @Test
  fun `Save two`() {
    // init data
    val po1 = randomYmd()
    val po2 = randomYmd()

    // invoke and verify
    dao.save(po1, po2).test().verifyComplete()
    assertEquals(po1, repository.getOne(po1.id))
    assertEquals(po2, repository.getOne(po2.id))
    repository.flush()
  }
}