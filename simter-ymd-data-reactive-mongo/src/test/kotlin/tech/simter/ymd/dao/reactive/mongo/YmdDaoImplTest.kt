package tech.simter.ymd.dao.reactive.mongo

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import tech.simter.ymd.dao.YmdDao

/**
 * See [SimpleReactiveMongoRepository] implementation.
 * @author RJ
 */
@Disabled
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
class YmdDaoImplTest @Autowired constructor(
  private val operations: ReactiveMongoOperations,
  private val dao: YmdDao
) {
  @Test
  fun test() {
    // TODO
  }
}