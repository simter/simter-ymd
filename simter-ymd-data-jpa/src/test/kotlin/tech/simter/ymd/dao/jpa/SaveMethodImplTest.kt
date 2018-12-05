package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.StepVerifier
import tech.simter.ymd.dao.YmdDao
import tech.simter.ymd.dao.jpa.TestUtils.randomYmd
import tech.simter.ymd.po.YmdPK

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

    // invoke
    val result = dao.save(po)

    // verify
    StepVerifier.create(result).verifyComplete()
    assertEquals(po, repository.getOne(YmdPK(type = po.type, year = po.year, month = po.month, day = po.day)))
    repository.flush()
  }

  @Test
  fun `Save two`() {
    // init data
    val po1 = randomYmd()
    val po2 = randomYmd()

    // invoke
    val result = dao.save(po1, po2)

    // verify
    StepVerifier.create(result).verifyComplete()
    assertEquals(po1, repository.getOne(YmdPK(type = po1.type, year = po1.year, month = po1.month, day = po1.day)))
    assertEquals(po2, repository.getOne(YmdPK(type = po2.type, year = po2.year, month = po2.month, day = po2.day)))
    repository.flush()
  }
}