package tech.simter.ymd.impl.dao.mongo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.mongo.po.YmdPo
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class CreateTest @Autowired constructor(
  private val repository: YmdRepository,
  private val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Create nothing`() {
    dao.create().test().verifyComplete()
    repository.count().test().expectNext(0).verifyComplete()
  }

  @Test
  fun `Create one`() {
    // init data
    val ymd = randomYmd()
    val po = YmdPo.from(ymd)

    // invoke and verify
    dao.create(ymd).test().verifyComplete()
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }

  @Test
  fun `Create two`() {
    // init data
    val ymd1 = randomYmd()
    val po1 = YmdPo.from(ymd1)
    val ymd2 = randomYmd()
    val po2 = YmdPo.from(ymd2)

    // invoke
    dao.create(ymd1, ymd2).test().verifyComplete()
    repository.findById(po1.id).test().expectNext(po1).verifyComplete()
    repository.findById(po2.id).test().expectNext(po2).verifyComplete()
  }
}