package tech.simter.ymd.dao.jpa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import tech.simter.ymd.dao.YmdDao

/**
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataJpaTest
class YmdDaoImplTest @Autowired constructor(
  val dao: YmdDao
) {
  @Test
  fun test() {
    // TODO
  }
}