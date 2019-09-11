package tech.simter.ymd.impl.dao.mongo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.mongo.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.save].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataMongoTest
class SaveMethodImplTest @Autowired constructor(
  private val repository: YmdRepository,
  private val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    repository.deleteAll().test().verifyComplete()
  }

  @Test
  fun `Save nothing`() {
    dao.save().test().verifyComplete()
    repository.count().test().expectNext(0).verifyComplete()
  }

  @Test
  fun `Save one`() {
    // init data
    val po = randomYmd()

    // invoke and verify
    dao.save(po).test().verifyComplete()
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }

  @Test
  fun `Save two`() {
    // init data
    val po1 = randomYmd()
    val po2 = randomYmd()

    // invoke
    dao.save(po1, po2).test().verifyComplete()
    repository.findById(po1.id).test().expectNext(po1).verifyComplete()
    repository.findById(po2.id).test().expectNext(po2).verifyComplete()
  }
}