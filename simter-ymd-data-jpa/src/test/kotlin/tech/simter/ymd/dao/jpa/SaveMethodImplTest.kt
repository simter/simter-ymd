package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.ymd.TestUtils.randomYmd
import tech.simter.ymd.dao.YmdDao

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class SaveMethodImplTest @Autowired constructor(
  private val repository: YmdJpaRepository,
  val dao: YmdDao
) {
  @Test
  fun `Save nothing`() {
    StepVerifier.create(dao.save()).verifyComplete()
    assertEquals(0, repository.count())
  }

  @Test
  fun `Save one`() {
    // init data
    val po = randomYmd()
    assertNull(po.id)

    // invoke
    val result = dao.save(po)

    // verify
    StepVerifier.create(result).verifyComplete()
    assertNotNull(po.id)
    assertEquals(po, repository.getOne(po.id!!))
    repository.flush()
  }

  @Test
  fun `Save two`() {
    // init data
    val po1 = randomYmd()
    val po2 = randomYmd()
    assertNull(po1.id)
    assertNull(po2.id)

    // invoke
    val result = dao.save(po1, po2)

    // verify
    StepVerifier.create(result).verifyComplete()
    assertNotNull(po1.id)
    assertNotNull(po2.id)
    assertEquals(po1, repository.getOne(po1.id!!))
    assertEquals(po2, repository.getOne(po2.id!!))
    repository.flush()
  }
}